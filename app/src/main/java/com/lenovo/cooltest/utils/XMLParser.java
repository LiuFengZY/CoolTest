package com.lenovo.cooltest.utils;

import java.io.IOException;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.lenovo.cooltest.MyFragmentHome;
import com.lenovo.cooltest.error.CommonError;

/**
 * Created by liufeng23 on 2017/8/22.
 */

public class XMLParser implements SimpleAsyncHttpClient.HttpCallback<String> {

    private Context mContext = null;
    private Handler mHandler = null;
    private String mMsgKey = "";
    private int mMsgValueId = 0;

    public XMLParser(Context context, Handler handler, String msgKey, int msgValueid) {
        // TODO:check null first.
        mContext = context;
        mHandler = handler;
        mMsgKey = msgKey;
        mMsgValueId = msgValueid;
    }

    public void doRequestXmlStringFromUrl(String url) {
        if (mContext == null || mHandler == null) {
            System.out.println("mContext = null, or mHandle is null!");
            onError(CommonError.ERR_NULL_CONTEXT);
            return;
        }
        SimpleAsyncHttpClient.doHttpRequest(SimpleAsyncHttpClient.HTTP_REQUEST_METHOD.HTTP_POST,
                url, this, true);
    }

    @Override
    public void onSuccess(String response) {
        System.out.println("lf, will send message.sucess:" + response);
        Message msg = mHandler.obtainMessage(mMsgValueId);
        Bundle bundle=new Bundle();
        bundle.putString(mMsgKey, response);
        msg.setData(bundle);
        mHandler.sendMessage(msg);
    }

    @Override
    public void onError(String error) {
        System.out.println("lf, error:" + error);
    }

    /**
     * 获取XML DOM元素
     * @param xml string
     * */
    public Document getDomElement(String xml){
        Document doc = null;
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        try {

            DocumentBuilder db = dbf.newDocumentBuilder();
            InputSource is = new InputSource();
            is.setCharacterStream(new StringReader(xml));
            doc = db.parse(is);

        } catch (ParserConfigurationException e) {
            Log.e("Error: ", e.getMessage());
            return null;
        } catch (SAXException e) {
            Log.e("Error: ", e.getMessage());
            return null;
        } catch (IOException e) {
            Log.e("Error: ", e.getMessage());
            return null;
        }

        return doc;
    }

    /** 获取节点值
     * @param elem element
     */
    public final String getElementValue( Node elem ) {
        Node child;
        if( elem != null){
            if (elem.hasChildNodes()){
                for( child = elem.getFirstChild(); child != null; child = child.getNextSibling() ){
                    if( child.getNodeType() == Node.TEXT_NODE  ){
                        return child.getNodeValue();
                    }
                }
            }
        }
        return "";
    }

    /**
     * 获取节点值
     * @param item node
     * @param str string
     * */
    public String getValue(Element item, String str) {
        NodeList n = item.getElementsByTagName(str);
        return this.getElementValue(n.item(0));
    }
}
