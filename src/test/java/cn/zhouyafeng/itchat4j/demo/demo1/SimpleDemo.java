package cn.zhouyafeng.itchat4j.demo.demo1;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import com.alibaba.fastjson.JSONObject;
import org.apache.log4j.Logger;

import cn.zhouyafeng.itchat4j.api.MessageTools;
import cn.zhouyafeng.itchat4j.api.WechatTools;
import cn.zhouyafeng.itchat4j.beans.BaseMsg;
import cn.zhouyafeng.itchat4j.beans.RecommendInfo;
import cn.zhouyafeng.itchat4j.core.Core;
import cn.zhouyafeng.itchat4j.face.IMsgHandlerFace;
import cn.zhouyafeng.itchat4j.utils.enums.MsgTypeEnum;
import cn.zhouyafeng.itchat4j.utils.tools.DownloadTools;

import static cn.zhouyafeng.itchat4j.utils.SleepUtils.sleep;

/**
 * 简单示例程序，收到文本信息自动回复原信息，收到图片、语音、小视频后根据路径自动保存
 *
 * @author https://github.com/yaphone
 * @version 1.0
 * @date 创建时间：2017年4月25日 上午12:18:09
 */
public class SimpleDemo implements IMsgHandlerFace {
    Logger LOG = Logger.getLogger(SimpleDemo.class);

    @Override
    public String textMsgHandle(BaseMsg msg) {
        // String docFilePath = "D:/itchat4j/pic/1.jpg"; // 这里是需要发送的文件的路径
        if (!msg.isGroupMsg()) { // 群消息不处理
            // String userId = msg.getString("FromUserName");
            // MessageTools.sendFileMsgByUserId(userId, docFilePath); // 发送文件
            // MessageTools.sendPicMsgByUserId(userId, docFilePath);
            String text = msg.getText(); // 发送文本消息，也可调用MessageTools.sendFileMsgByUserId(userId,text);
            LOG.info("接收" + text);
            if (text.equals("111")) {
                WechatTools.logout();
            }
            if (text.equals("222")) {
                WechatTools.remarkNameByNickName("陈晓旭｜鱼鱼禾尔®精品有机海苔", "CXX");
            }
            if (text.equals("333")) { // 测试群列表
                // System.out.print(WechatTools.getGroupNickNameList());
                // System.out.print(WechatTools.getGroupList());
                // System.out.print(WechatTools.getGroupIdList());
                // System.out.print(Core.getInstance().getGroupMemeberMap());
                System.out.print(Core.getInstance().getGroupIdNameMap());
            }
            if (text.equals("444")) {
                Map<String, String> gMap = Core.getInstance().getGroupIdNameMap();
                String roomId = gMap.get("测试群发1");
                WechatTools.sendMsgByUserName("你好，我是小助手，请输入：111退出，222修改备注，333群列表，444发送文件", roomId);
            }
            if (text.equals("555")) {
                List<JSONObject> objects = WechatTools.searchUserNameByNickName("测试");
                System.out.println(objects);
            }
            if (text.equals("888")) {
                String mediaId = MessageTools.uploadMedia("/Users/wangrong/project/ruoyi-vue-pro/yudao-server/img" +
                        "/login/2024-12-12-21-50-22.jpg");
                System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + ">>>>>>>>>>");
                // for (int i = 1; i <= 100; i++) {
                //     if (!MessageTools.sendPicByUserId("filehelper", mediaId)) {
                //         System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) +
                //                 "###########");
                //         break;
                //     }
                //     System.out.println("NO-" + i);
                //     // sleep(2500);
                // }
                int i = 1;
                while (true) {
                    if (!MessageTools.sendPicByUserId("filehelper", mediaId)) {
                        if (i == 1) {
                            break;
                        }
                        System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) +
                                "###########");
                        i = 1;
                        sleep(60000);
                    }
                    System.out.println("NO-" + i + " " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
                    i++;
                }
                System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) +
                        "@@@@@@@@@@@@");
            }
            return text;
        }
        return null;
    }

    @Override
    public String picMsgHandle(BaseMsg msg) {
        String fileName = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss").format(new Date());// 这里使用收到图片的时间作为文件名
        String picPath = "D://itchat4j/pic" + File.separator + fileName + ".jpg"; // 调用此方法来保存图片
        DownloadTools.getDownloadFn(msg, MsgTypeEnum.PIC.getType(), picPath); // 保存图片的路径
        return "图片保存成功";
    }

    @Override
    public String voiceMsgHandle(BaseMsg msg) {
        String fileName = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss").format(new Date());
        String voicePath = "D://itchat4j/voice" + File.separator + fileName + ".mp3";
        DownloadTools.getDownloadFn(msg, MsgTypeEnum.VOICE.getType(), voicePath);
        return "声音保存成功";
    }

    @Override
    public String viedoMsgHandle(BaseMsg msg) {
        String fileName = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss").format(new Date());
        String viedoPath = "D://itchat4j/viedo" + File.separator + fileName + ".mp4";
        DownloadTools.getDownloadFn(msg, MsgTypeEnum.VIEDO.getType(), viedoPath);
        return "视频保存成功";
    }

    @Override
    public String nameCardMsgHandle(BaseMsg msg) {
        return "收到名片消息";
    }

    @Override
    public void sysMsgHandle(BaseMsg msg) { // 收到系统消息
        String text = msg.getContent();
        LOG.info(text);
    }

    @Override
    public String verifyAddFriendMsgHandle(BaseMsg msg) {
        MessageTools.addFriend(msg, true); // 同意好友请求，false为不接受好友请求
        RecommendInfo recommendInfo = msg.getRecommendInfo();
        String nickName = recommendInfo.getNickName();
        String province = recommendInfo.getProvince();
        String city = recommendInfo.getCity();
        String text = "你好，来自" + province + city + "的" + nickName + "， 欢迎添加我为好友！";
        return text;
    }

    @Override
    public String mediaMsgHandle(BaseMsg msg) {
        String fileName = msg.getFileName();
        String filePath = "D://itchat4j/file" + File.separator + fileName; // 这里是需要保存收到的文件路径，文件可以是任何格式如PDF，WORD，EXCEL等。
        DownloadTools.getDownloadFn(msg, MsgTypeEnum.MEDIA.getType(), filePath);
        return "文件" + fileName + "保存成功";
    }

}
