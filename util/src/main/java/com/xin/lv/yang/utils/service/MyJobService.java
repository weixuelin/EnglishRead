package com.xin.lv.yang.utils.service;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.os.Build;
import android.support.annotation.RequiresApi;

/**
 * 任务调度服务 后台服务
 */

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class MyJobService  extends JobService{
    @Override
    public boolean onStartJob(JobParameters jobParameters) {
        //完成后释放资源
        jobFinished(jobParameters,true);
        return false;
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        return false;
    }
}
