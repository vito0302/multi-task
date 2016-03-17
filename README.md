# multi-task
[![Build Status](https://travis-ci.org/wangchongjie/multi-task.svg?branch=master)](https://travis-ci.org/wangchongjie/multi-task)
[![Coverage Status](https://coveralls.io/repos/github/wangchongjie/multi-task/badge.svg?branch=master)](https://coveralls.io/github/wangchongjie/multi-task?branch=master)
![](https://maven-badges.herokuapp.com/maven-central/com.baidu.unbiz/multi-task/badge.svg)

Multi-task is a Java framework for parallel processing which is based annotation.

## 1. Quick Start
This chapter will show you how to get started with Multi-Task.

### 1.1 Prerequisite

In order to use Multi-Task within a Maven project, simply add the following dependency to your pom.xml. 
```
	<dependency>
    	<groupId>com.baidu.unbiz</groupId>
    	<artifactId>multi-task</artifactId>
    	<version>1.0.0</version>
	</dependency>
```
### 1.2 Create a normal service with annotation

Create a normal service class whose methods will be called parallely on later. For example, a DevicePlanStatServiceImpl class is created as below.

```
@TaskService
public class DevicePlanStatServiceImpl implements DevicePlanStatService {
    
    @TaskBean("deviceStatFetcher")
    public List<DeviceViewItem> queryPlanDeviceData(DeviceRequest req) {
        this.checkParam(req);
        return this.mockList1();
    }

    @TaskBean("deviceUvFetcher")
    public List<DeviceViewItem> queryPlanDeviceUvData(DeviceRequest req) {
        this.checkParam(req);
        return this.mockList2();
    }
    
    @TaskBean("multiParamFetcher")
    public List<DeviceViewItem> queryPlanDeviceDataByMultiParam(String p1, int p2, int p3) {
        return this.mockList1();
    }

    @TaskBean("voidParamFetcher")
    public List<DeviceViewItem> queryPlanDeviceDataByVoidParam() {
        return this.mockList2();
    }
    
    @TaskBean("doSthFailWithExceptionFetcher")
    public List<DeviceViewItem> queryPlanDeviceDataWithBusinessException(DeviceRequest req) {
        throw new BusinessException("Some business com.baidu.unbiz.multitask.vo.exception, just for test!");
    }
    
    @TaskBean("doSthVerySlowFetcher")
    public List<DeviceViewItem> queryPlanDeviceDataWithBadNetwork(DeviceRequest req) {
        try {
            Thread.sleep(900000L);
        } catch (InterruptedException e) {
            // do nothing, just for test
        }
        return this.mockList1();
    }
}
```

### 1.3 Applying parallely processing with defined @TaskBean task

```
    @Resource(name = "simpleParallelExePool")
    private ParallelExePool parallelExePool;

    public void testParallelFetch() {
        DeviceRequest req1 = new DeviceRequest();
        DeviceRequest req2 = new DeviceRequest();

        TaskContext ctx =
                parallelExePool.submit(
                        new TaskPair("deviceStatFetcher", req1),
                        new TaskPair("deviceUvFetcher", req2));

        List<DeviceViewItem> stat = ctx.getResult("deviceStatFetcher");
        List<DeviceViewItem> uv = ctx.getResult("deviceUvFetcher");

        Assert.notEmpty(stat);
        Assert.notEmpty(uv);
    }
```
