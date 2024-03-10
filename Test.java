import java.util.ArrayList;
import java.util.Collections;
import javax.swing.*;
import java.awt.*;

public class Test {
    public static void main(String[] args) {

        // System.out.println("start");
        // BinomialHeap bh = new BinomialHeap();
        // bh.insert(6, "a");
        // bh.insert(15, "b");
        // bh.insert(1, "c");
        // bh.insert(2, "d");
        // bh.insert(3, "e");
        // bh.insert(1, "f");
        // BinomialHeap.HeapItem x = bh.insert(8, "g");
        // bh.insert(5, "h");
        // bh.insert(14, "i");
        // bh.insert(5, "j");
        // BinomialHeap.HeapItem y = bh.insert(9, "k");
        // bh.insert(7, "l");
        // bh.insert(13, "m");
        // bh.insert(4, "n");
        // bh.insert(11, "o");

        // bh.deleteMin();
        // bh.delete(x);
        // bh.deleteMin();
        // bh.decreaseKey(y, 8);
        // int n = bh.size();
        // for (int k = 0; k < n - 1; k++) {
        // bh.deleteMin();
        // }
        // bh.deleteMin();
        // bh.insert(5, "h");
        // HeapGraph.draw(bh);
        // System.out.println("done");

        // System.out.println("start");
        // int i = 4;
        // int n = (int) Math.pow(3, i + 5);
        // BinomialHeap bh = new BinomialHeap();
        // long startTime = System.currentTimeMillis();
        // for (int k = 1; k < n; k++) {
        // bh.insert(k, "");
        // }
        // long doneTime = System.currentTimeMillis();
        // // System.out.println("min: " + bh.min.item.key + ", size: " + bh.size);
        // System.out.println("running time:" + (doneTime - startTime));
        // System.out.println("number of links:" + BinomialHeap.connectionCounter);
        // System.out.println("number of trees:" + bh.numTrees());
        // System.out.println("done");

        System.out.println("start");
        int i = 1;
        int n = (int) Math.pow(3, i + 5);
        ArrayList<Integer> nums = new ArrayList<Integer>();
        BinomialHeap bh = new BinomialHeap();
        for (int k = 1; k < n; k++) {
            nums.add(k);
        }
        Collections.shuffle(nums);
        long startTime = System.currentTimeMillis();
        for (int k = 0; k < n - 1; k++) {
            bh.insert(nums.get(k), "");
        }
        System.out.println("size before deletion:" + bh.size);
        for (int k = 0; k < n / 2; k++) {
            bh.deleteMin();
        }
        long doneTime = System.currentTimeMillis();
        System.out.println("size after deletion:" + bh.size);
        System.out.println("running time:" + (doneTime - startTime));
        System.out.println("number of links:" + BinomialHeap.connectionCounter);
        System.out.println("number of trees:" + bh.numTrees());
        // System.out.println("deleted rank sum:" + BinomialHeap.rankCounter);
        System.out.println("done");

        // System.out.println("start");
        // int i = 1;
        // int n = (int) Math.pow(3, i + 5);
        // BinomialHeap bh = new BinomialHeap();
        // long startTime = System.currentTimeMillis();
        // for (int k = n; k > 1; k--) {
        // bh.insert(k - 1, "");
        // }
        // while (bh.size >= Math.pow(2, 5)) {
        // bh.deleteMin();
        // }
        // long doneTime = System.currentTimeMillis();
        // System.out.println("running time:" + (doneTime - startTime));
        // System.out.println("number of links:" + BinomialHeap.connectionCounter);
        // System.out.println("number of trees:" + bh.numTrees());
        // // System.out.println("deleted rank sum:" + BinomialHeap.rankCounter);
        // System.out.println("done");

    }
}
