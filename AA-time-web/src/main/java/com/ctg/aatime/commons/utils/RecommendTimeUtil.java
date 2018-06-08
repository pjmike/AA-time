package com.ctg.aatime.commons.utils;

import com.ctg.aatime.domain.Activity;
import com.ctg.aatime.domain.ActivityMembers;
import com.ctg.aatime.domain.dto.BestTime;
import com.ctg.aatime.domain.dto.RecommendTimeInfo;

import java.util.*;

/**
 * 推荐时间工具类
 * 调用getBestTimes，返回BestTimeInfo列表
 * Note：时间轴是从1开始计数
 * Created By Cx On 2018/4/5 11:51
 */
public class RecommendTimeUtil {
    /**
     * 将时间戳转换成时间块大小，每个时间块为15min
     *
     * @param time 时长
     * @return 该时长总共能分成多少个时间块
     */
    private static int getTimeSize(long time) {
        return (int)Math.ceil(time*1.0/1000/60/15) ;
    }

    /**
     * 将时间戳转换成时间块大小，每个时间块为15min
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return 该时间范围总共有多少个时间块
     */
    private static int getTimeSize(long startTime, long endTime) {
        return getTimeSize(endTime - startTime);
    }

    /**
     * 将时间块<开始时间块，结束时间块>转换成时间戳
     *
     * @param baseTime  活动统计开始时间戳
     * @param startTime 活动统计开始后的第几个时间块开始（从1开始）
     * @param endTime   活动统计开始后的第几个时间块结束（从2开始）比如描述第一个时间块：endTime=2，startTime=1
     * @return List:0表示startTime时间戳，1表示endTime时间戳
     */
    private static List<Long> formatTime(long baseTime, int startTime, int endTime) {
        List<Long> values = new ArrayList<Long>();
        //因为第一块的开始时间其实就是baseTime（活动可选开始时间），所以要减一
        values.add(baseTime + (startTime-1) * 15 * 60 * 1000);
        values.add(baseTime + (endTime-1) * 15 * 60 * 1000);
        return values;
    }

    /**
     * 生成时间轴
     * 例：活动最小时长2小时，活动可选时间开始时间是：2018.1.23,结束时间是：2018.1.24
     * 那么第1块就是2018.1.23.00：00——2018.1.23.02：00，第2块是2018.1.23.00：15——2018.1.23.02：15
     * 每个时间轴元素单位长度为最小时长，相邻时间轴元素相差15min（即时间块大小）
     * 时间轴元素个数：
     * （可选时间结束时间戳-开始时间戳）/1000/60/15（可选时间时间块数量） - 最小时长时间戳/1000/60/15（最小时长时间块数量）+1
     * 参与活动成员信息如下：
     * 第1块：3人，第2块：5人，第3块：7人，……，第96块（一天24小时共96个块）：10人
     * 则时间轴为3 5 7 …… 10
     *
     * @param activity 活动
     * @param members  报名参与人员
     * @return 由报名参与人员组成的时间轴
     */
    private static List<ActivityMembers>[] getTimes(Activity activity, List<ActivityMembers> members) {
        //活动可选时间范围,从1开始计数
        int timeSize = getTimeSize(activity.getStartTime(), activity.getEndTime());
        //活动最少持续时间
        int minTime = getTimeSize(activity.getMinTime());
        //若最少持续时间大于可选时间
        if (minTime > timeSize) return null;
        List<ActivityMembers>[] times = new ArrayList[timeSize - minTime + 5];

        //初始化times
        for (int i = 0; i < times.length; i++) {
            times[i] = new ArrayList<ActivityMembers>();
        }

        for (ActivityMembers member : members) {
            //遍历所有成员
            //跳过未选时间成员
            if (member.getFreeTimes() == null) continue;
            for (long key : member.getFreeTimes().keySet()) {
                //遍历每个成员的所有空闲时间区间
                //将开始时间和结束时间转换成时间块形式,表示是第x块的start/end
                int start = getTimeSize(key - activity.getStartTime()) + 1,
                        end = getTimeSize(member.getFreeTimes().get(key) - activity.getStartTime());
                //TODO 有无可能选取空闲时间超出可选时间范围。
                if (start < 1) {
                    start = 1;
                }
                if (end > timeSize) {
                    end = timeSize;
                }
                if (start > end || start > timeSize) {
                    //如果数据有误，抛弃该数据
                    continue;
                }
                if (end - start < minTime-1) {
                    //如果空闲区间长度小于最小时限,则该区间不满足
                    continue;
                }
                while (end - start >= minTime-1) {
                    //空闲区间长度大于等于最小时限，
                    //起点每次往右加一.每个起点是不同的时间范围,若满足则在该时间范围添加该成员
                    times[start].add(member);
                    start++;
                }
            }
        }
        return times;
    }

    /**
     * 获取创建者的所有空闲时间段
     * @return
     */
    private static Set<Integer> getUserTime(Activity activity,ActivityMembers user){
        //活动可选时间范围,从1开始计数
        int timeSize = getTimeSize(activity.getStartTime(), activity.getEndTime());
        //活动最少持续时间
        int minTime = getTimeSize(activity.getMinTime());
        //若最少持续时间大于可选时间
        if (minTime > timeSize) return null;
        Set<Integer> timeSet = new HashSet<Integer>();
        for (long key : user.getFreeTimes().keySet()) {
            //遍历每个成员的所有空闲时间区间
            //将开始时间和结束时间转换成时间块形式,表示是第x块的start/end
            int start = getTimeSize(key - activity.getStartTime()) + 1,
                    end = getTimeSize(user.getFreeTimes().get(key) - activity.getStartTime());
            if (start < 1) {
                start = 1;
            }
            if (end > timeSize) {
                end = timeSize;
            }
            if (start > end || start > timeSize) {
                //如果数据有误，抛弃该数据
                continue;
            }
            if (end - start < minTime-1) {
                //如果空闲区间长度小于最小时限,则该区间不满足
                continue;
            }
            while (end - start >= minTime-1) {
                //空闲区间长度大于等于最小时限，
                //起点每次往右加一.每个起点是不同的时间范围,若满足则在该时间范围添加该成员
                timeSet.add(start);
                start++;
            }
        }
        return timeSet;
    }

    /**
     * 返回最优解信息
     *
     * @return
     */
    public static RecommendTimeInfo getRecommendTimeInfo(Activity activity, List<ActivityMembers> members,ActivityMembers user) {
        if (activity.getMinTime() < 15*60*1000L){
            //如果最小时间小于15min，默认改为15min
            activity.setMinTime((long) (15*60*1000));
        }
        //最优解列表
        List<BestTime> bestTimes = new ArrayList<BestTime>();
        //活动可选时间范围
        int timeSize = getTimeSize(activity.getStartTime(), activity.getEndTime());
        //活动最少持续时间
        int minTime = getTimeSize(activity.getMinTime());
        //返回的推荐时间信息,membersNum加1，因为参与人列表和创建者是分开的，所以需要加上创建者
        RecommendTimeInfo recommendTimeInfo = new RecommendTimeInfo(bestTimes, activity.getStartTime(), activity.getEndTime(), members.size()+1);
        if (user.getFreeTimes() == null || timeSize < minTime){
            //如果创建者未选择时间 或 活动可选范围小于活动最小时限，直接返回空列表
            return recommendTimeInfo;
        }
        //创建者空闲时间段
        Set<Integer> uTime = getUserTime(activity,user);
        //TODO
//        for (ActivityMembers m : members) {
//            for (long d : m.getFreeTimes().keySet()){
//                System.out.print(Test2.stampToDate(d)+"="+Test2.stampToDate(m.getFreeTimes().get(d))+" ");
//            }
//            System.out.println();
//            Set<Integer> m1 = getUserTime(activity,m);
//            System.out.println(m.getId()+" "+m1);
//        }
//        System.out.println(uTime);

        //定义时间轴数组（每个元素长为最小时长，元素下标表示起始时间块）,值为参与人集合，最优解即为元素值最大的元素。
        List<ActivityMembers>[] times = getTimes(activity, members);

        //用于记录最优解中的key值，使其能够按key值的增序输出
        List<Integer> keyList = new ArrayList<Integer>();
        //最优解的参与成员数
        int maxMember = 0;
        //最优解时间段，key表示起始时间，value表示结束时间
        Map<Integer, Integer> bestTime = new HashMap<Integer, Integer>();

        //遍历times数组
        for (int i = 1; i <= timeSize; i++) {
            if (uTime.contains(i)){
                if (times[i].size() > maxMember) {
                    //如果找到比已知最大参与人数大的时间范围
                    bestTime.clear();
                    bestTime.put(i, i + minTime);
                    maxMember = times[i].size();
                } else if (times[i].size() == maxMember) {
                    //如果找到和已知最大参与人数相同的时间范围
                    bestTime.put(i, i + minTime);
                }
            }
        }

        //将最优解的key放入keyList排序
        for (int key : bestTime.keySet()) {
            keyList.add(key);
        }
        //由小到大排序keyList
        Collections.sort(keyList);

        //合并可合并的连续范围
        for (int i = 0; i < keyList.size() - 1; i++) {
            //遍历所有最优解
            int key1 = keyList.get(i), key2 = keyList.get(i + 1);
            if (bestTime.get(key1) >= key2) {
                //如果第一个解的截止时间大于等于第二个解的起始时间，说明有重合则看是否能合并，flag=1则可以合并
                int flag = 1;
                for (int j = 0; j < times[key1].size(); j++) {
                    ActivityMembers m = times[key1].get(j);
                    if (!times[key2].contains(m)) {
                        //第二个解不包含第一个解的成员,则不能合并
                        flag = 0;
                        break;
                    }
                }
                if (flag == 1) {
                    //若可以合并获取第二个解的截止时间,移除第二个解，更新第一个解
                    int end = bestTime.get(key2);
                    bestTime.remove(key2);
                    keyList.remove(i + 1);
                    bestTime.put(key1, end);
                    i--;//下次继续搜索该位置
                }
            }
        }

        //将处理完成的最优解扔进最优解列表
        for (int key : bestTime.keySet()) {
            List<ActivityMembers> notJoin = members;
            for (ActivityMembers am : times[key]) {
                notJoin.remove(am);
            }
            List<Long> l = formatTime(activity.getStartTime(), key, bestTime.get(key));
            //加上创建者
            times[key].add(user);
            BestTime bti = new BestTime(l.get(0), l.get(1), times[key], notJoin);
            bestTimes.add(bti);
        }
        recommendTimeInfo.setBestTimes(bestTimes);
        return recommendTimeInfo;
    }

}
