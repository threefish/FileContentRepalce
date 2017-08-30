import com.sun.java.swing.plaf.motif.MotifLookAndFeel;
import com.sun.java.swing.plaf.windows.WindowsLookAndFeel;
import org.jb2011.lnf.beautyeye.BeautyEyeLNFHelper;

import javax.swing.*;
import javax.swing.plaf.FontUIResource;
import javax.swing.plaf.metal.MetalLookAndFeel;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.regex.Pattern;

/**
 * Created with IntelliJ IDEA.
 * User: 黄川
 * Date Time: 2015/2/1314:21
 */

public class MainFrom {
    private JPanel rootPanel;
    private JLabel lable4;
    private JButton startbutton;
    private JTextField inputfilePath;
    private JButton choseFilePathbutton;
    private JLabel lable1;
    private JLabel lable2;
    private JLabel lable3;
    private JTextField filehouzui;
    private JTextField oldcontetn;
    private JTextField newcontent;
    private JLabel lable0;
    private JButton startButton;
    private JTextArea msgpan;
    private static ArrayList fileList = new ArrayList();


    public static void main(String[] args) {
        try {
            Font fnt = new Font("Microsoft YaHei UI", Font.PLAIN, 14);
            FontUIResource fontRes = new FontUIResource(fnt);
            for (Enumeration keys = UIManager.getDefaults().keys(); keys.hasMoreElements(); ) {
                Object key = keys.nextElement();
                Object value = UIManager.get(key);
                if (value instanceof FontUIResource)
                    UIManager.put(key, fontRes);
            }
            String theme="BeautyEye";
            switch (theme) {
                case "BeautyEye":
                    BeautyEyeLNFHelper.launchBeautyEyeLNF();
                    UIManager.put("RootPane.setupButtonVisible", false);
                    break;
                case "系统默认":
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                    break;
                case "Windows":
                    UIManager.setLookAndFeel(WindowsLookAndFeel.class.getName());
                    break;
                case "Nimbus":
                    UIManager.setLookAndFeel(NimbusLookAndFeel.class.getName());
                    break;
                case "Metal":
                    UIManager.setLookAndFeel(MetalLookAndFeel.class.getName());
                    break;
                case "Motif":
                    UIManager.setLookAndFeel(MotifLookAndFeel.class.getName());
                    break;
                case "Darcula(推荐)":
                default:
                    UIManager.setLookAndFeel("com.bulenkov.darcula.DarculaLaf");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        JFrame frame = new JFrame("MainFrom");
        frame.setTitle("文件内容批量替换工具---by QQ:306955302");
        frame.setResizable(false);
        int w = 600, h = 600;
        int x = (int) (Toolkit.getDefaultToolkit().getScreenSize().getWidth() / 2 - (w / 2));
        int y = (int) (Toolkit.getDefaultToolkit().getScreenSize().getHeight() / 2 - (h / 2));
        frame.setContentPane(new MainFrom().rootPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
        frame.setBounds(x, y, w, h);

    }

    public MainFrom() {
        /**
         * 选择文件目录
         */
        choseFilePathbutton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser chooser = new JFileChooser();
                chooser.setFileSelectionMode(1);// 设定只能选择到文件夹
                int returnVal = chooser.showOpenDialog(rootPanel);// 此句是打开文件选择器界面的触发语句
                if (returnVal == 1) {
                    return;
                } else {
                    File f = chooser.getSelectedFile();// f为选择到的目录
                    inputfilePath.setText(f.getAbsolutePath());
                    msgpan.setText("目录已选择..........\r\n");
                }
            }
        });
        /**
         * 开始替换
         */
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                fileList = new ArrayList();
                SwingWorker worker = new SwingWorker() {
                    @Override
                    protected Object doInBackground() throws Exception {
                        msgpan.setText("");
                        choseFilePathbutton.setEnabled(false);
                        startButton.setEnabled(false);
                        Pattern regx = Pattern.compile(".*\\." + filehouzui.getText().toLowerCase());
                        msgpan.setText("开始扫描文件..........\r\n");
                        /**
                         * 查找文件
                         */
                        searchFile(new File(inputfilePath.getText()), regx, fileList);


                        msgpan.setText(msgpan.getText() + "文件总数:" + fileList.size() + "\r\n");
                        /**
                         * 替换文件
                         */
                        msgpan.setText(msgpan.getText() + "替换开始!" + "\r\n");
                        msgpan.setText(msgpan.getText() + "替换开始!----start" + "\r\n");
                        for (int i = 0; i < fileList.size(); i++) {
                            replaceFile(String.valueOf(fileList.get(i)), oldcontetn.getText(), newcontent.getText());
                        }
                        msgpan.setText(msgpan.getText() + "替换完成!----end" + "\r\n");
                        msgpan.setText(msgpan.getText() + "完成!!!" + "\r\n");
                        choseFilePathbutton.setEnabled(true);
                        startButton.setEnabled(true);
                        return null;
                    }
                };

                worker.execute();
            }
        });
    }

    /**
     * 根据规则扫描文件
     * @param folder
     * @param regex
     * @param list
     */
    public void searchFile(File folder, Pattern regex, ArrayList list) {
        File[] files = folder.listFiles();
        if (files == null) {
            msgpan.setText(msgpan.getText() + "不能访问：" + folder.getAbsolutePath() + "\r\n");
            return;
        }
        for (File file : files) {
            if (file.isDirectory()) {
                searchFile(file, regex, fileList);
            } else {
                if (regex.matcher(file.getName().toLowerCase()).matches()) {
                    fileList.add(file.getAbsolutePath());
                }
            }
        }
    }

    /**
     * 根据规则替换文件内容
     * @param path
     * @param luanStr
     * @param newStr
     */
    public void replaceFile(String path, String luanStr, String newStr) {
        File file = new File(path);
        String encoding = "utf-8";
        InputStreamReader read = null;
        String content = "";
        try {
            read = new InputStreamReader(new FileInputStream(file), encoding);
            BufferedReader bufferedReader = new BufferedReader(read);
            String TxtBuff = null;
            boolean luanFlag = false;
            while ((TxtBuff = bufferedReader.readLine()) != null) {
                if (TxtBuff.indexOf(luanStr) > -1) {
                    luanFlag = true;
                    TxtBuff = TxtBuff.replaceAll(luanStr, newStr);
                }
                content += TxtBuff + "\r\n";
            }
            try {
                read.close();
            } catch (Exception e) {
            }
            if (luanFlag) {
                FileOutputStream out = new FileOutputStream(path);
                OutputStreamWriter writer = new OutputStreamWriter(out, encoding);
                try {
                    writer.write(content);
                    writer.close();
                    msgpan.setText(msgpan.getText() + "完成：" + path + "\r\n");
                } catch (Exception e) {
                    msgpan.setText(msgpan.getText() + "失败：" + path + "\r\n");
                    out.close();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
