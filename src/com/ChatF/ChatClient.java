package com.ChatF;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

//JFrame 을 사용하여 윈도우 창을 띄워준다. -> JFrame 은 스윙 클래스의 일, 구현되는 하나의 창
//끊임없는 데이터 교환을 위한 Runnable 을 implements
//자바 Swing -> 자바의 GUI 패키지 ----> AWT 보다 가볍고 컴포넌트가 자바로 작성되어 있어 어떤 플랫폼에서도 일관된 화면 작성 가능
public class ChatClient extends JFrame implements ActionListener, Runnable {
    private JTextArea output;
    private JTextField input;
    private JButton sendBtn;
    private Socket socket; //소켓
    private ObjectInputStream reader = null;    //객체를 직렬화하여 저장
    private ObjectOutputStream writer = null;   //직렬화한 데이터를 역직렬화

    private String nickName;

    public ChatClient() {
        //센터에 TextArea 만들기
        output = new JTextArea();
        output.setFont(new Font("맑은 고딕", Font.BOLD, 15));
        output.setEditable(false);
        JScrollPane scroll = new JScrollPane(output);
        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        //하단에 버튼과 TextArea 넣기
        JPanel bottom = new JPanel();
        bottom.setLayout(new BorderLayout());
        input = new JTextField();

        sendBtn = new JButton("보내기");
        //가운데 정렬
        bottom.add("Center", input);
        //오른쪽 정렬
        bottom.add("East", sendBtn);

        Container c = this.getContentPane();
        //가운데 정렬
        c.add("Center", scroll);
        //아래쪽 정렬
        c.add("South", bottom);
        //윈도우 창 설정
        setBounds(300, 300, 300, 300);
        setVisible(true);

        this.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                try {
                    InfoDTO dto = new InfoDTO();
                    dto.setNickName(nickName);
                    dto.setCommand(Info.EXIT);
                    writer.writeObject(dto);
                    writer.flush();
                }catch (IOException e2){
                    e2.printStackTrace();
                }
            }
        });
    }

    public static void main(String[] args) {
        new ChatClient().service();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            //JTextField 의 값을 서버로 보냄
            String msg = input.getText();
            InfoDTO dto = new InfoDTO();

            if (msg.equals("exit")) {
                dto.setCommand(Info.EXIT);
            } else {
                dto.setCommand(Info.SEND);
                dto.setMessage(msg);
                dto.setNickName(nickName);
            }
            writer.writeObject(dto);
            writer.flush();
            input.setText("");
        }catch (IOException e2) {
            e2.printStackTrace();
        }
    }

    public void service() {
        //서버 IP 입력받기
        String serverIp = JOptionPane.showInputDialog(this, "서버 IP를 입력하세요", "127.0.0.1");
        //값이 입력되지않으면 서버 꺼짐
        if (serverIp==null || serverIp.length()==0) {
            System.out.println("서버 IP가 입력되지 않았습니다.");
            System.exit(0);
        }

        nickName = JOptionPane.showInputDialog(this, "닉네임을 입력하세요", "닉네임", JOptionPane.INFORMATION_MESSAGE);
        if (nickName==null || nickName.length()==0) {
            nickName="guest";
        }

        try {
            socket = new Socket(serverIp, 1234);
            reader = new ObjectInputStream(socket.getInputStream());
            writer = new ObjectOutputStream(socket.getOutputStream());
            System.out.println("전송 준비 완료");
        } catch (UnknownHostException e) {
            System.out.println("서버를 찾을 수 없습니다.");
            e.printStackTrace();
            System.exit(0);
        } catch (IOException e2) {
            System.out.println("서버와 연결되지 않았습니다.");
            e2.printStackTrace();
            System.exit(0);
        }

        try {
            InfoDTO dto = new InfoDTO();
            dto.setCommand(Info.JOIN);
            dto.setNickName(nickName);
            writer.writeObject(dto);
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Thread thread = new Thread(this);
        thread.start();
        input.addActionListener(this);
        sendBtn.addActionListener(this);
    }

    @Override
    public void run() {
        InfoDTO dto = null;
        while (true) {
            try {
                dto = (InfoDTO) reader.readObject();
                if (dto.getCommand()==Info.EXIT) {
                    reader.close();
                    writer.close();
                    socket.close();
                    System.exit(0);
                } else if (dto.getCommand()==Info.SEND) {
                    output.append(dto.getMessage() + "\n");

                    int pos = output.getText().length();
                    output.setCaretPosition(pos);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e2) {
                e2.printStackTrace();
            }
        }
    }
}
