package com.wz.study.distributedlock.nativezookeeper;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.io.IOException;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

public class DistributedLock implements Lock, Watcher {

    private ZooKeeper zk = null;
    private String ROOT_LOCK = "/locks";//定义根节点
    private String WAIT_LOCK;//等待前一个锁
    private String CURRENT_LOCK;//表示当前锁
    private CountDownLatch countDownLatch;

    static {
        try {
            ZooKeeper zk = new ZooKeeper("127.0.0.1:2181", 4000, null);
            //判断根节点是否存在
            Stat stat = zk.exists("/locks", false);
            if (stat == null) {
                System.out.println(Thread.currentThread().getName() + " root_lock not exists,create it!");
                zk.create("/locks", "0".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
            } else {
                System.out.println(Thread.currentThread().getName() + " root_lock already exists");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (KeeperException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public DistributedLock() {
      //根节点放到类中做添加
        try {
            zk = new ZooKeeper("127.0.0.1:2181", 4000, this);
            //System.out.println(Thread.currentThread().getName() + " connect success!");
            //判断根节点是否存在
            Stat stat = zk.exists(ROOT_LOCK, false);
            if (stat == null) {
                System.out.println(Thread.currentThread().getName() + " root_lock not exists,create it!");
                zk.create(ROOT_LOCK, "0".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
            } else {
                System.out.println(Thread.currentThread().getName() + " root_lock already exists");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (KeeperException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void lock() {
        if (tryLock()) {
            System.out.println(Thread.currentThread().getName() + "->" + CURRENT_LOCK + "->获得锁成功");
        }else{
            long  before =System.currentTimeMillis();
            //没有获得锁则继续等待获得锁
            waitForLock(WAIT_LOCK);
            long  after =System.currentTimeMillis();
            System.out.println(Thread.currentThread().getName() + "->经过等待"+(after-before)+"毫秒后获得锁成功");
        }
    }

    private void waitForLock(String prev) {
        try {
            //监听当前节点的上一个节点
            Stat stat = zk.exists(prev, true);
            if (stat != null) {
                //等待上一个节点释放锁，等待过程使用CountDownLatch处理
                System.out.println(Thread.currentThread().getName() + " ->等待锁" + ROOT_LOCK + "/" + prev + "释放");
                countDownLatch = new CountDownLatch(1);
                //等待的过程在process中处理（处理完成后countDownLatch.countDown()）
                countDownLatch.await();
            }
        } catch (KeeperException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void lockInterruptibly() throws InterruptedException {

    }

    @Override
    public boolean tryLock() {
        try {
            CURRENT_LOCK = zk.create(ROOT_LOCK + "/", "0".getBytes(),
                    ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
            System.out.println(Thread.currentThread().getName() + "->" + CURRENT_LOCK + ",尝试竞争锁");

            //获取根节点下的所有节点，对节点进行排序处理
            List<String> childs = zk.getChildren(ROOT_LOCK, false);
            SortedSet<String> sortedSet = new TreeSet();//通过treeSet定义一个集合进行排序
            for (String child : childs) {
                sortedSet.add(ROOT_LOCK + "/" + child);
            }
            //获得当前所有子节点中最小的节点
            String firstNode = sortedSet.first();
            //通过当前的节点和子节点中最小的子节点进行比较，如果相等，表示获得锁成功
            if (CURRENT_LOCK.equals(firstNode)) {
                return true;
            }

            //判断当前节点是否是最小的节点(返回此 set 的部分视图，其元素严格小于 toElement                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                1   )
            SortedSet<String> lessThanMe = sortedSet.headSet(CURRENT_LOCK);
            if (!lessThanMe.isEmpty()) {
                //获得比当前节点更小的最后一个节点，设置给WAIT_LOCK`
                WAIT_LOCK = lessThanMe.last();
            }
        } catch (KeeperException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
        return false;
    }

    @Override
    public void unlock() {
        try {
            System.out.println(Thread.currentThread().getName() + "->释放锁" + CURRENT_LOCK);
            //删除当前节点，version设置为-1表示，不管三七二十一，统一删
            zk.delete(CURRENT_LOCK, -1);
            //将当前节点设置为空
            CURRENT_LOCK = null;
            //关闭zk连接
            zk.close();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (KeeperException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Condition newCondition() {
        return null;
    }

    @Override
    public void process(WatchedEvent watchedEvent) {
        if (this.countDownLatch != null) {
            this.countDownLatch.countDown();
        }
    }
}
