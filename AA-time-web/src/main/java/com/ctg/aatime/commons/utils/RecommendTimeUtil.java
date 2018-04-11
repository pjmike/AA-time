package com.ctg.aatime.commons.utils;

import com.ctg.aatime.domain.Activity;
import com.ctg.aatime.domain.ActivityMembers;
import com.ctg.aatime.domain.dto.BestTime;
import com.ctg.aatime.domain.dto.RecommendTimeInfo;

import java.util.*;

/**
 * 推荐时间工具类
 * 调用getBestTimes，返回BestTimeInfo列表
 * Created By Cx On 2018/4/5 11:51
 */
public class RecommendTimeUtil {

    /**
     * 将时间戳转换成时间块大小，每个时间块为15min
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return
     */
    public static int getTimeSize(long startTime, long endTime){
        int size = (int)((endTime - startTime)/1000/60/15);
        return size;
    }

    public static int getTimeSize(long time){
        int size = (int)(time/1000/60/15);
        return size;
    }

    public static List<ActivityMembers>[] getTimes(Activity activity, List<ActivityMembers> members) {
        //活动可选时间范围
        int timeSize = getTimeSize(activity.getStartTime(), activity.getEndTime());
        //活动最少持续时间
        int minTime = getTimeSize(activity.getMinTime());
        List<ActivityMembers>[] times = new ArrayList[timeSize - minTime + 1];

        //初始化times
        for (int i = 0 ; i < timeSize - minTime + 1; i++) {
            times[i] = new ArrayList<ActivityMembers>();
        }

        for (ActivityMembers member : members) {
            //遍历所有成员
            for (long key : member.getFreeTimes().keySet()) {
                //遍历每个成员的所有空闲时间区间
                //将开始时间和结束时间转换成时间块形式
                int start = getTimeSize(key - activity.getStartTime()),
                        end = getTimeSize(member.getFreeTimes().get(key) - activity.getStartTime());
                //TODO 有无可能数据异常，即选取空闲时间超出可选时间范围。
                if (end - start + 1 < minTime) {
                    //如果空闲区间长度小于最小时限,则该区间不满足
                    continue;
                }
                while (end - start + 1 >= minTime) {
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
     * 返回最优解信息
     * @return
     */
    public static RecommendTimeInfo getRecommendTimeInfo(Activity activity, List<ActivityMembers> members) {

        //活动可选时间范围
        int timeSize = getTimeSize(activity.getStartTime(), activity.getEndTime());
        //活动最少持续时间
        int minTime = getTimeSize(activity.getMinTime());
        //最优解列表
        List<BestTime> bestTimes = new ArrayList<BestTime>();
        //所有推荐信息
        RecommendTimeInfo recommendTimeInfo = new RecommendTimeInfo(bestTimes,activity.getStartTime(),activity.getEndTime(),members.size());

        if (timeSize < minTime){
            //若活动可选范围小于活动最小时限，直接返回空列表
            return recommendTimeInfo;
        }

        //定义时间块数组（每个块表示长度为最小时限15min的时间范围）,值为参与人集合，最优解即为元素值最大的时间块。
        List<ActivityMembers>[] times = getTimes(activity, members);
        //用于记录最优解中的key值，使其能够按key值的增序输出
        List<Integer> keyList = new ArrayList<Integer>();
        //最优解的参与成员数
        int maxMember = 0;
        //最优解时间段
        Map<Integer,Integer> bestTime = new HashMap<Integer,Integer>();

        //遍历times数组
        for (int i = 0; i < times.length; i++) {
            if (times[i].size() > maxMember) {
                //如果找到比已知最大参与人数大的时间范围
                bestTime.clear();
                bestTime.put(i, i + minTime - 1);
                maxMember = times[i].size();
            }
            else if (times[i].size() == maxMember) {
                //如果找到和已知最大参与人数相同的时间范围
                bestTime.put(i, i + minTime -1);
            }
        }

        //将最优解的key放入keyList排序
        for (int key : bestTime.keySet()) {
            keyList.add(key);
        }
        //由小到大排序keyList
        Collections.sort(keyList);

        //合并可合并的连续范围
        for (int i = 0; i < keyList.size() - 1; i++){
            //遍历所有最优解
            int key1 = keyList.get(i),key2 = keyList.get(i + 1);
            if (bestTime.get(key1) >= key2) {
                //如果第一个解的截止时间大于等于第二个解的起始时间，说明有重合则看是否能合并，flag=1则可以合并
                int flag = 1;
                for (int j = 0; j < times[key1].size();j++){
                    ActivityMembers m = times[key1].get(j);
                    if ( !times[key2].contains(m) ){
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
                    bestTime.put(key1,end);
                    i--;//下次继续搜索该位置
                }
            }
        }

        //将处理完成的最优解扔进最优解列表
        for (int key : bestTime.keySet()) {
            BestTime bti = new BestTime(key,bestTime.get(key),times[key]);
            bestTimes.add(bti);
        }
        recommendTimeInfo.setBestTimes(bestTimes);
        return recommendTimeInfo;
    }



}
