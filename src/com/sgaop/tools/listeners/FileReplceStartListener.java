package com.sgaop.tools.listeners;

import com.sgaop.tools.ui.FileReplceContent;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;

/**
 * Created with IntelliJ IDEA.
 *
 * @author 306955302@qq.com
 * 创建人：黄川
 * 创建时间: 2017/11/30  11:44
 * 描述此类：
 */
public class FileReplceStartListener {
    public static void addListener() {
        FileReplceContent.instance.bStart.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SwingWorker worker = new SwingWorker() {
                    @Override
                    protected Object doInBackground() throws Exception {
                        Path path = Paths.get(FileReplceContent.instance.textFilePath.getText());
                        String suffix = FileReplceContent.instance.textFileSuffix.getText();
                        String oldContent = FileReplceContent.instance.textOldContent.getText();
                        String newContent = FileReplceContent.instance.textNewContent.getText();
                        Files.walkFileTree(path, new FileReplceStartListener.FindFile(suffix, oldContent, newContent));
                        return null;
                    }
                };
                worker.execute();
            }
        });
    }

    private static class FindFile extends SimpleFileVisitor<Path> {

        private String suffix;
        private String oldContent;
        private String newContent;

        public FindFile(String suffix, String oldContent, String newContent) {
            this.suffix = suffix;
            this.oldContent = oldContent;
            this.newContent = newContent;
        }

        @Override
        public FileVisitResult visitFile(Path path, BasicFileAttributes attrs) {
            String filePath = path.toString();
            if (filePath.endsWith(suffix)) {
                File file = path.toFile();
                try (InputStreamReader read = new InputStreamReader(new FileInputStream(file), Charset.forName("utf-8"))) {
                    BufferedReader bufferedReader = new BufferedReader(read);
                    String TxtBuff = null;
                    boolean hasOldContent = false;
                    StringBuffer sb = new StringBuffer();
                    while ((TxtBuff = bufferedReader.readLine()) != null) {
                        if (TxtBuff.indexOf(oldContent) > -1) {
                            hasOldContent = true;
                            TxtBuff = TxtBuff.replace(oldContent, newContent);
                        }
                        sb.append(TxtBuff);
                        sb.append("\r\n");
                    }
                    bufferedReader.close();
                    if (hasOldContent) {
                        FileOutputStream out = new FileOutputStream(file);
                        OutputStreamWriter writer = new OutputStreamWriter(out, Charset.forName("utf-8"));
                        writer.write(sb.toString());
                        writer.flush();
                        writer.close();
                        out.close();
                        FileReplceContent.instance.textLogs.append("成功："+path.getFileName().toString()+"\r\n");
                        FileReplceContent.instance.textLogs.setCaretPosition(FileReplceContent.instance.textLogs.getDocument().getLength());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    FileReplceContent.instance.textLogs.append("失败："+path.getFileName().toString()+"\r\n");
                    FileReplceContent.instance.textLogs.setCaretPosition(FileReplceContent.instance.textLogs.getDocument().getLength());
                }
            }
            return FileVisitResult.CONTINUE;
        }
    }
}
