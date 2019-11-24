package me.zhengjie.utils;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import com.baidu.aip.imageclassify.AipImageClassify;
import com.baidu.aip.ocr.AipOcr;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Baidu AI相关的接口
 * 
 * 获取token类
 */
public class BaiduUtils {
	/**
	 * 用于车牌识别和车辆检测
	 */
	public static final String APP_ID = "17012014";
	public static final String API_KEY = "HrD5eGGy5kKSqZMYnGtm2MB1";
	public static final String SECRET_KEY = "3fIR6UbrLc16aKrCoVL8A7bcxfKFhCQt";
	public static String AUTH_TOKEN="24.146dde76b31f0b03246d76fd47ec5026.2592000.1568360335.282335-17012014";
	
	/**
	 * 用户车辆类型识别
	 */
	public static final String EASYDL_APP_ID = "17201835";
	public static final String EASYDL_API_KEY = "iixwrPmxclw1QgzQXctfPdLB";
	public static final String EASYDL_SECRET_KEY = "bltumUzbH9tRsns4jx81YpTBHtXKvrdC";
	public static String EASYDL_AUTH_TOKEN="24.1e3d3cf1696f1647fa3956749a8007d6.2592000.1570611743.282335-17201835";
	
	public static void main(String[] args)
	{
		//桂MB3381
		String path = "C:/download/pictrue/pictrue/truck/LVAABVx3rGaEN_P1AAAAAEskMiU571.jpg";
        String imgStr = FileUtil.file2Base64(new File(path));
            
        //System.out.println(getAuthToken(API_KEY, SECRET_KEY));
        //System.out.println(getAuthToken(EASYDL_API_KEY, EASYDL_SECRET_KEY));
		//System.out.print(vehicleDetect(imgStr));
		//carBrand();
		//System.out.println(licensePlate(imgStr));
        System.out.println(vehicleType(imgStr));
	}

    /**
     * 获取API访问token
     * 该token有一定的有效期，需要自行管理，当失效时需重新获取.
     * @param ak - 百度云官网获取的 API Key
     * @param sk - 百度云官网获取的 Securet Key
     * @return assess_token 示例：
     * "24.460da4889caad24cccdb1fea17221975.2592000.1491995545.282335-1234567"
     */
    public static String getAuthToken(String ak, String sk) {
        // 获取token地址
        String authHost = "https://aip.baidubce.com/oauth/2.0/token?";
        String getAccessTokenUrl = authHost
                // 1. grant_type为固定参数
                + "grant_type=client_credentials"
                // 2. 官网获取的 API Key
                + "&client_id=" + ak
                // 3. 官网获取的 Secret Key
                + "&client_secret=" + sk;
        try {
            URL realUrl = new URL(getAccessTokenUrl);
            // 打开和URL之间的连接
            HttpURLConnection connection = (HttpURLConnection) realUrl.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();
            // 获取所有响应头字段
            Map<String, List<String>> map = connection.getHeaderFields();
            // 遍历所有的响应头字段
            for (String key : map.keySet()) {
                System.err.println(key + "--->" + map.get(key));
            }
            // 定义 BufferedReader输入流来读取URL的响应
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String result = "";
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
            /*
             * * 返回结果中获取token
             */
            System.err.println("result:" + result);
            JSONObject jsonObject = new JSONObject(result);
            String access_token = jsonObject.getString("access_token");
            return access_token;
        } catch (Exception e) {
            System.err.printf("获取token失败！");
            e.printStackTrace(System.err);
        }
        return null;
    }
    
	/**
	 * 车辆检测，货车，卡车等，同时可以检测出车牌的数量
	 * https://ai.baidu.com/docs#/ImageClassify-API/2180f77d
	 */
	public static String vehicleDetect(String base64File)
	{
		String access_token = "24.146dde76b31f0b03246d76fd47ec5026.2592000.1568360335.282335-17012014";
        
        String api_str = "https://aip.baidubce.com/rest/2.0/image-classify/v1/vehicle_detect?access_token=" + access_token;

        String Content_Type ="application/x-www-form-urlencoded";

		JSONObject res=null;
		
		try {            
            CloseableHttpClient httpclient = HttpClients.createDefault();
            
            HttpPost httpPost = new HttpPost(api_str);
            httpPost.setHeader("Content-Type", Content_Type);
            List <NameValuePair> nvps = new ArrayList <NameValuePair>();
            nvps.add(new BasicNameValuePair("image", base64File));
            httpPost.setEntity(new UrlEncodedFormEntity(nvps));
                       
            CloseableHttpResponse response2 = httpclient.execute(httpPost);

            try {
                HttpEntity entity2 = response2.getEntity();
                String result = EntityUtils.toString(entity2);
                JSONObject jsonObject = new JSONObject(result);
                return jsonObject.getString("access_token");
            }catch(Exception e)
            {
            	e.printStackTrace();
            }
            finally {
                response2.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
	
		return null;
	}
	
	/**
	 * 自定义的easydl车辆检测，现在模型支持三种类型，truck（货车），danger（危险品车辆），bus（客车）。
	 */
	public static String vehicleType(String base64File)
	{
	    String auth_code = getAuthToken(EASYDL_API_KEY, EASYDL_SECRET_KEY);
        String api_str = "https://aip.baidubce.com/rpc/2.0/ai_custom/v1/classification/vehicle_type?access_token=" + auth_code;

        String Content_Type ="application/json";

		JSONObject res=null;
		
		try {            
            CloseableHttpClient httpclient = HttpClients.createDefault();
            
            HttpPost httpPost = new HttpPost(api_str);
            httpPost.setHeader("Content-Type", Content_Type);
            
            JSONObject params = new JSONObject();
            params.put("image", base64File);
            params.put("top_num", "1");
            StringEntity s = new StringEntity(params.toString());
            s.setContentEncoding("UTF-8");
            s.setContentType(Content_Type);

            httpPost.setEntity(s);
            
            CloseableHttpResponse response2 = httpclient.execute(httpPost);

            try {
                HttpEntity entity2 = response2.getEntity();
                String result = EntityUtils.toString(entity2);
                System.out.println("type result:"+result);
                JSONObject jsonObject = new JSONObject(result);
                return ((JSONObject)jsonObject.getJSONArray("results").get(0)).getString("name");
            } catch(Exception e)
            {
            	e.printStackTrace();
            }finally {
                response2.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
	
		return "";
	}
	
	/**
	 * 车牌识别等
	 */
	public static String licensePlate(String base64File)
	{
		String access_token = getAuthToken(API_KEY, SECRET_KEY);
        
        String api_str = "https://aip.baidubce.com/rest/2.0/ocr/v1/license_plate?access_token=" + access_token;

        String Content_Type ="application/x-www-form-urlencoded";

		JSONObject res=null;
		
		try {
            CloseableHttpClient httpclient = HttpClients.createDefault();
            
            HttpPost httpPost = new HttpPost(api_str);
            httpPost.setHeader("Content-Type", Content_Type);
            List <NameValuePair> nvps = new ArrayList <NameValuePair>();
            nvps.add(new BasicNameValuePair("image", base64File));
            httpPost.setEntity(new UrlEncodedFormEntity(nvps));
            
            CloseableHttpResponse response2 = httpclient.execute(httpPost);

            try {
                HttpEntity entity2 = response2.getEntity();
                String result = EntityUtils.toString(entity2);
                System.out.println("plate result:"+result);
                JSONObject jsonObject = new JSONObject(result);
                String plate = jsonObject.getJSONObject("words_result").getString("number");
                
                return plate;
            }catch(Exception e)
            {
            	e.printStackTrace();
            }
            finally {
                response2.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
		
		return "";
	}
	
	/**
	 * 车辆品牌识别，比如大众T5，铃木五十铃等
	 */
	public static String carBrand()
	{
		AipOcr client = new AipOcr(APP_ID,API_KEY,SECRET_KEY);
		
		client.setConnectionTimeoutInMillis(2000);
		client.setSocketTimeoutInMillis(60000);
		
		HashMap<String,String> options = new HashMap<>();
		options.put("multi_detect", "false");
		
		//桂MB3381
		String path = "C:/download/pictrue/pictrue/truck/LVAABVx3rGaEN_P1AAAAAEskMiU571.jpg";
		JSONObject result=null;
		
		//JSONObject res = client.plateLicense(path, options);
		//System.out.println(res.toString(2));
		
		//识别成：铃木北斗星
		AipImageClassify clientImg = new AipImageClassify(APP_ID, API_KEY, SECRET_KEY);

        // 可选：设置网络连接参数
		clientImg.setConnectionTimeoutInMillis(2000);
		clientImg.setSocketTimeoutInMillis(60000);

        // 调用接口
		result = clientImg.carDetect(path, new HashMap<String, String>());      
        
        System.out.println(result.toString(2));
        
        JSONObject jsonObject = new JSONObject(result);
        String access_token = jsonObject.getString("access_token");
        return access_token;
	}
}