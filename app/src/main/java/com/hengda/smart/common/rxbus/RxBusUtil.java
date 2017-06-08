package com.hengda.smart.common.rxbus;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.subscriptions.CompositeSubscription;

/**
 * 作者：Tailyou （祝文飞）
 * 时间：2016/7/23 14:05
 * 邮箱：tailyou@163.com
 * 描述：
 */
public class RxBusUtil {

    /**
     * 订阅RxBus事件
     *
     * @param subscription
     * @param eventType
     * @param action1
     */
    public static <T> void subscribeEvent(CompositeSubscription subscription,
                                          Class<T> eventType,
                                          Action1<T> action1) {
        if (subscription == null)
            subscription = new CompositeSubscription();
        subscription.add(RxBus.getDefault()
                .toObservable(eventType)
                .onBackpressureBuffer()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(action1));
    }

    /**
     * 取消订阅RxBus事件
     *
     * @param subscription
     */
    public static void unsubscribe(CompositeSubscription subscription) {
        if (subscription != null && subscription.hasSubscriptions()) {
            subscription.clear();
            subscription.unsubscribe();
        }
    }

    /**
     * 订阅RxBus事件
     *
     * @param eventType
     * @param action1
     */
    public static <T> Subscription subscribeEvent(Class<T> eventType,
                                                  Action1<T> action1) {
        return RxBus.getDefault()
                .toObservable(eventType)
                .onBackpressureBuffer()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(action1);
    }

    /**
     * 取消订阅RxBus事件
     *
     * @param subscription
     */
    public static void unsubscribe(Subscription subscription) {
        if (subscription != null && !subscription.isUnsubscribed()) {
            subscription.unsubscribe();
        }
    }

}
