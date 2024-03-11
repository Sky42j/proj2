import javax.swing.*;
import java.awt.*;

public class HeapGraph extends Canvas {
    public BinomialHeap heap;

    public HeapGraph(BinomialHeap heap) {
        this.heap = heap;
    }

    public static int rankSum(BinomialHeap heap) {
        BinomialHeap.HeapNode node = heap.last.next;
        int sum = node.rank;
        if (sum == 0)
            sum = 1;
        node = node.next;
        while (node != heap.last.next) {
            sum += node.rank;
            node = node.next;
        }
        return sum;
    }

    public void paintHeap(BinomialHeap heap, Graphics g, int y) {
        int unit = this.getWidth() / (rankSum(heap) * 2);
        BinomialHeap.HeapNode tree = heap.last.next;
        int lastBorder = tree.rank * unit;
        if (lastBorder == 0)
            lastBorder = unit;
        paintTree(tree, g, 0, lastBorder, y);
        while (!tree.equals(heap.last)) {
            tree = tree.next;
            paintTree(tree, g, lastBorder, lastBorder + tree.rank * unit, y);
            lastBorder += tree.rank * unit;
        }
    }

    public static void retrieve(BinomialHeap.HeapNode node) {
        BinomialHeap.HeapNode last = node.child, find = node.child;
        while (!find.next.equals(last))
            find = find.next;
        node.child = find;
        find.next = last.next;
        node.rank = node.rank - 1;
    }

    public static void connect(BinomialHeap.HeapNode node, BinomialHeap.HeapNode branch) {
        node.child.next = branch;
        node.child = branch;
        node.rank = node.rank + 1;
    }

    public void paintTree(BinomialHeap.HeapNode node, Graphics g, int in, int out, int y) {
        int mid = (in + out) / 2;
        if (node.rank == 0) {
            g.setColor(new Color(153, 192, 227));
            g.fillOval(mid - 25, y, 50, 50);
            g.setColor(Color.BLACK);
            FontMetrics fm = g.getFontMetrics();
            String key = String.valueOf(node.item.key);
            g.drawString(key, mid - fm.stringWidth(key) / 2, y + 35);
        } else if (node.rank == 1) {
            BinomialHeap.HeapNode branch = node.child;
            paintTree(branch, g, in, out, y + 150);
            retrieve(node);
            paintTree(node, g, in, out, y);
            connect(node, branch);
            g.drawLine(mid, y + 50, mid, y + 150);
        } else {
            int hOffset = (out - in) / (int) Math.pow(2, node.rank);
            BinomialHeap.HeapNode branch = node.child;
            g.drawLine(mid - hOffset, y + 175, out - hOffset, y + 25);
            paintTree(branch, g, in, mid, y + 150);
            retrieve(node);
            paintTree(node, g, mid, out, y);
            connect(node, branch);

        }
    }

    @Override
    public void paint(Graphics g) {
        Font f = new Font("Cambria Math", Font.BOLD, 26);
        g.setFont(f);
        Graphics2D g2 = (Graphics2D) g;
        g2.setStroke(new BasicStroke(3));
        paintHeap(heap, g, 30);
    }

    public static void draw(BinomialHeap heap) {
        JFrame frame = new JFrame();
        HeapGraph heapGraph = new HeapGraph(heap);
        heapGraph.setSize(1500, 750);
        heapGraph.setBackground(Color.WHITE);
        frame.add(heapGraph);
        frame.pack();
        frame.setVisible(true);
    }
}