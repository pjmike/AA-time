package com.ctg.aatime.domain.dto;

import com.ctg.aatime.domain.Activity;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author pjmike
 * @create 2018-05-30 21:34
 */
@Data
public class ActivityDto extends Activity {
    private MultipartFile file;
}
