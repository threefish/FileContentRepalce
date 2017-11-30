package com.sgaop.tools;

import com.sgaop.tools.listeners.FileReplceStartListener;
import com.sgaop.tools.ui.FileReplceContent;

import javax.swing.*;
import javax.swing.plaf.FontUIResource;
import java.awt.*;
import java.util.Enumeration;

/**
 * Created with IntelliJ IDEA.
 *
 * @author 306955302@qq.com
 * 创建人：黄川
 * 创建时间: 2017/11/30  11:29
 * 描述此类：
 */
public class StartApp {
    public static void main(String[] args) {
        initTheme();
        FileReplceContent.frame = new JFrame("文件内容批量替换工具---by QQ:306955302");
        FileReplceContent.instance = new FileReplceContent();
        int w = 500, h = 350;
        FileReplceContent.frame.setResizable(false);
        FileReplceContent.frame.getRootPane().setOpaque(false);
        int x = (int) (Toolkit.getDefaultToolkit().getScreenSize().getWidth() / 2 - (w / 2));
        int y = (int) (Toolkit.getDefaultToolkit().getScreenSize().getHeight() / 2 - (h / 2));
        FileReplceContent.frame.setContentPane(FileReplceContent.instance.root);
        FileReplceContent.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        FileReplceContent.frame.pack();
        FileReplceContent.frame.setVisible(true);
        FileReplceContent.frame.setBounds(x, y, w, h);
        FileReplceStartListener.addListener();
    }


    private static void initTheme() {
        try {
            Font fnt = new Font("Microsoft YaHei UI", Font.PLAIN, 14);
            FontUIResource fontRes = new FontUIResource(fnt);
            for (Enumeration keys = UIManager.getDefaults().keys(); keys.hasMoreElements(); ) {
                Object key = keys.nextElement();
                Object value = UIManager.get(key);
                if (value instanceof FontUIResource) {
                    UIManager.put(key, fontRes);
                }
            }
            UIManager.setLookAndFeel("com.bulenkov.darcula.DarculaLaf");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
