package com.redimybase.flowable.msg.handle;

import com.redimybase.flowable.msg.dto.NoticeDTO;

/**
 * 任务通知接口
 * Created by Vim 2018/11/21 0:14
 *
 * @author Vim
 */
public interface NoticeHandler {
    public void handle(NoticeDTO noticeDTO);


}
