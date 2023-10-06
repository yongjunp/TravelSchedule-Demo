package com.TravelSchedule.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

import org.springframework.stereotype.Service;

import com.TravelSchedule.dto.Tdest;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

@Service
public class ApiService {

	private final String ServiceKey = "S7zgFEQqSlrWVhHdRtINMDDNv%2BTnaJrW2iEOUsm2Y5UdcfYh6inhqrsA1Qls%2BtLEub4iFJ4UT89YTfsFhZ0sZQ%3D%3D";

	public ArrayList<Tdest> getTdestList() throws Exception {

		StringBuilder urlBuilder = new StringBuilder(
				"https://apis.data.go.kr/B551011/KorService1/areaBasedList1"); /* URL */
		urlBuilder.append("?" + URLEncoder.encode("serviceKey", "UTF-8") + "=" + ServiceKey); /* Service Key */
		urlBuilder.append(
				"&" + URLEncoder.encode("pageNo", "UTF-8") + "=" + URLEncoder.encode("1", "UTF-8")); /* 페이지 번호 */
		urlBuilder.append("&" + URLEncoder.encode("numOfRows", "UTF-8") + "="
				+ URLEncoder.encode("6000", "UTF-8")); /* 한 페이지 결과 수 */
		urlBuilder.append("&" + URLEncoder.encode("MobileOS", "UTF-8") + "=" + URLEncoder.encode("ETC", "UTF-8"));
		urlBuilder.append(
				"&" + URLEncoder.encode("MobileApp", "UTF-8") + "=" + URLEncoder.encode("TravelSchedule", "UTF-8"));
		urlBuilder.append(
				"&" + URLEncoder.encode("_type", "UTF-8") + "=" + URLEncoder.encode("json", "UTF-8")); /* XML/JSON 여부 */
		urlBuilder.append(
				"&" + URLEncoder.encode("areaCode", "UTF-8") + "=" + URLEncoder.encode("1", "UTF-8")); /* 지역코드 */
		urlBuilder.append("&" + URLEncoder.encode("contentTypeId", "UTF-8") + "=" + URLEncoder.encode("12", "UTF-8"));

		URL url = new URL(urlBuilder.toString());
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("GET");
		conn.setRequestProperty("Content-type", "application/json");
		System.out.println("Response code: " + conn.getResponseCode());
		BufferedReader rd;
		if (conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
			rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		} else {
			rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
		}
		StringBuilder sb = new StringBuilder();
		String line;
		while ((line = rd.readLine()) != null) {
			sb.append(line);
		}
		rd.close();
		conn.disconnect();
//		System.out.println(sb.toString());

		// json 변환
		JsonObject DestInfo = (JsonObject) JsonParser.parseString(sb.toString());
//		System.out.println("[DestInfo] : " + DestInfo);

		JsonArray infoList = DestInfo.get("response").getAsJsonObject().get("body").getAsJsonObject().get("items")
				.getAsJsonObject().get("item").getAsJsonArray();

//		System.out.println("[infoList] : " + infoList);

		JsonObject DestInfo_Body = DestInfo.get("response").getAsJsonObject().get("body").getAsJsonObject();

		JsonObject DestInfo_Items = DestInfo_Body.get("items").getAsJsonObject();

//		System.out.println("[DestInfo_Body] : " + DestInfo_Body);
//		System.out.println("[DestInfo_Itmes] : " + DestInfo_Items);

//	        JsonArray itemList = DestInfo_Items.get("item").getAsJsonArray();

//	        System.out.println("[itemList] : "+itemList);
//	        System.out.println("[tiemList.size] : "+itemList.size());

		ArrayList<Tdest> TdestList = new ArrayList<Tdest>();
		for (int i = 0; i < infoList.size(); i++) {
			Tdest tdest = new Tdest();
			String tdname = infoList.get(i).getAsJsonObject().get("title").getAsString();
			tdest.setTdname(tdname);

			String addr1 = infoList.get(i).getAsJsonObject().get("addr1").getAsString();
			String addr2 = infoList.get(i).getAsJsonObject().get("addr2").getAsString();
			String tdaddress = addr1 + " " + addr2;
			tdest.setTdaddress(tdaddress);

			String tdphoto = infoList.get(i).getAsJsonObject().get("firstimage").getAsString();
			tdest.setTdphoto(tdphoto);

			TdestList.add(tdest);
		}
	//	System.out.println(TdestList);
		return TdestList;
	}
}