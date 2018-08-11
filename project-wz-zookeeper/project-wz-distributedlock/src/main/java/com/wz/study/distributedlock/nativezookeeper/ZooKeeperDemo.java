package com.wz.study.distributedlock.nativezookeeper;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.xml.bind.SchemaOutputResolver;
import java.io.IOException;
import java.util.Collections;
import java.util.List;


public class ZooKeeperDemo {
    private String ROOT_LOCK = "/locks";//定义根节点
    private String WAIT_LOCK;//等待前一个锁
    private String CURRENT_LOCK;//表示当前锁

    public static void main(String[] args) {

    }

    private ZooKeeper zk;

    @Before
    public void setUp() throws IOException {
        System.out.println("come set_up");
        try {
            zk = new ZooKeeper("127.0.0.1:2181", 4000, null);
            //判断根节点是否存在
            Stat stat = zk.exists("/locks", false);
            if (stat == null) {
                System.out.println(Thread.currentThread().getName() + " root_lock not exists,create it!");
                zk.create("/locks", "0".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
            } else {
                //System.out.println(Thread.currentThread().getName() + " root_lock already exists");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (KeeperException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @After
    public void destroy() throws InterruptedException {
        System.out.println("come destroy");
        zk.close();
    }

    @Test
    public void query() throws KeeperException, InterruptedException {
        System.out.println("come query");
        List<String> children = zk.getChildren(ROOT_LOCK, false);
        Collections.sort(children);
        children.stream().forEach(System.out::println);
    }

    @Test
    public void delete() throws KeeperException, InterruptedException {
        System.out.println("come delete");
        List<String> children = zk.getChildren(ROOT_LOCK, false);
        Collections.sort(children);
        children.stream().forEach((n)->{
            try {
                zk.delete(ROOT_LOCK + "/"+n,-1);
                System.out.println("delete "+ROOT_LOCK + "/"+n);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (KeeperException e) {
                e.printStackTrace();
            }
        });
        System.out.println("------after delete---------");
        this.query();
    }

    @Test
    public void addNode() {
        System.out.println("come addNode");
        try {
            for (int i = 0; i < 10; i++) {
                String s = zk.create(ROOT_LOCK+"/", "0".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
                System.out.println("add a node:"+s);
            }
            System.out.println("------after addNode---------");
            this.query();
            System.out.println("------before delete ------------");
            this.delete();

        } catch (KeeperException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
