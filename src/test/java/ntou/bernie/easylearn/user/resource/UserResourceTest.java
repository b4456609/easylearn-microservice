package ntou.bernie.easylearn.user.resource;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.List;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;

import com.fasterxml.jackson.databind.*;
import io.dropwizard.testing.junit.ResourceTestRule;
import ntou.bernie.easylearn.user.db.UserDAO;
import org.bson.types.ObjectId;
import org.junit.ClassRule;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonParseException;

import ntou.bernie.easylearn.user.core.User;
import org.mockito.Mock;

public class UserResourceTest {

	private ObjectMapper objectMapper = new ObjectMapper()
			.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
			.setPropertyNamingStrategy(PropertyNamingStrategy.CAMEL_CASE_TO_LOWER_CASE_WITH_UNDERSCORES);

	private static UserDAO userDAO = mock(UserDAO.class);

	@ClassRule
	public static final ResourceTestRule resources = ResourceTestRule.builder()
			.addResource(new UserResource(userDAO))
			.build();

	@Test
	public void testUserJsonDeserialize() throws JsonParseException, JsonMappingException, IOException {
		String json = "{\"id\":\"1009840175700426\",\"name\":\"\u8303\u632F\u539F\",\"setting\":{\"wifi_sync\":true,\"mobile_network_sync\":true,\"last_sync_time\":1450325981000,\"modified\":true,\"version\":25},\"bookmark\":[],\"folder\":[]}";

		User user = objectMapper.readValue(json, User.class);		
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		Validator validator = factory.getValidator();
		Set<ConstraintViolation<User>> constraintViolations =
			      validator.validate( user );
		assertEquals(0,constraintViolations.size());
	}

	@Test
	public void testSyncUser() throws IOException {
		String json = "{\n" +
				"  \"user\": {\n" +
				"    \"id\": \"1009840175700426\",\n" +
				"    \"name\": \"范振原\",\n" +
				"    \"setting\": {\n" +
				"      \"wifi_sync\": true,\n" +
				"      \"mobile_network_sync\": true,\n" +
				"      \"last_sync_time\": 1455874940000,\n" +
				"      \"modified\": false,\n" +
				"      \"version\": 26\n" +
				"    }\n" +
				"  },\n" +
				"  \"folder\": [\n" +
				"    {\n" +
				"      \"name\": \"全部的懶人包\",\n" +
				"      \"id\": \"allfolder\",\n" +
				"      \"pack\": [\n" +
				"        \"pack1439355907459\",\n" +
				"        \"pack1439367500493\",\n" +
				"        \"pack1439370245981\",\n" +
				"        \"pack1439372921598\",\n" +
				"        \"pack1439381800612\",\n" +
				"        \"pack1439385129482\",\n" +
				"        \"pack1439394796784\",\n" +
				"        \"pack1439451391246\",\n" +
				"        \"pack1439471856230\"\n" +
				"      ]\n" +
				"    },\n" +
				"    {\n" +
				"      \"name\": \"All\",\n" +
				"      \"id\": \"allPackId\",\n" +
				"      \"pack\": [\n" +
				"        \"pack1439355907459\",\n" +
				"        \"pack1439370245981\",\n" +
				"        \"pack1439372921598\",\n" +
				"        \"pack1439381800612\",\n" +
				"        \"pack1439385129482\",\n" +
				"        \"pack1439394796784\",\n" +
				"        \"pack1450275282853\",\n" +
				"        \"pack1450347155192\"\n" +
				"      ]\n" +
				"    },\n" +
				"    {\n" +
				"      \"name\": \"我的最愛\",\n" +
				"      \"id\": \"fjoeiwjowfe\",\n" +
				"      \"pack\": []\n" +
				"    },\n" +
				"    {\n" +
				"      \"name\": \"與你分享懶人包\",\n" +
				"      \"id\": \"shareFolder\",\n" +
				"      \"pack\": [\n" +
				"        \"pack1439394796784\"\n" +
				"      ]\n" +
				"    }\n" +
				"  ],\n" +
				"  \"pack1439355907459\": {\n" +
				"    \"creator_user_id\": \"1009840175700426\",\n" +
				"    \"cover_filename\": \"62oLIgA.jpg\",\n" +
				"    \"create_time\": 1439355907000,\n" +
				"    \"creator_user_name\": \"范振原\",\n" +
				"    \"name\": \"一次了解「八仙樂園粉塵爆炸事件」\",\n" +
				"    \"is_public\": true,\n" +
				"    \"description\": \"水上樂園粉塵爆炸造成500多人灼傷，打破台灣救災史一次燒燙傷人數的最新紀錄\",\n" +
				"    \"version\": [\n" +
				"      {\n" +
				"        \"creator_user_id\": \"1009840175700426\",\n" +
				"        \"note\": [],\n" +
				"        \"create_time\": 1439357781000,\n" +
				"        \"pack_id\": \"pack1439355907459\",\n" +
				"        \"user_view_count\": 0,\n" +
				"        \"version\": 0,\n" +
				"        \"content\": \"<p><strong>時間：</strong>6月27日晚間約8點30分左右</p>\\n<p><strong>地點：</strong>新北市八仙樂園</p>\\n<p><strong>活動：</strong>玩色創意公司舉辦「Color Play Asia - 彩色派對」活動</p>\\n<p><strong>傷亡狀況：</strong>495人受傷，10人死亡（至7月23日為止有343人住院，在加護病房213名，病危162名）</p>\\n<p><strong>官方處理：</strong>新北市長朱立倫下令八仙樂園無限期停業。內政部宣布活動禁用可燃性微細粉末。</p>\\n<p><img id=\\\"TIXHiEy \\\" style=\\\"max-width: 100% !important; height: auto; display: block;\\\" src=\\\"FILE_STORAGE_PATHpack1439355907459/TIXHiEy.gif\\\" alt=\\\"\\\" /></p>\\n<p>&nbsp;</p>\\n<p><strong>民眾怎麼幫助爆炸傷者？</strong></p>\\n<p><strong>1.捐款</strong></p>\\n<p>截至目前為止，新北市愛心專戶收到10萬2144筆捐款、累計10億8680萬9046元。12日召開八仙粉塵氣爆案受害家屬代表會議，共有23位各大醫院受害家屬推派的代表出席，會中推選出7人成為善款專戶管理委員會委員。</p>\\n<p>&nbsp;</p>\\n<p><strong><span style=\\\"color: #319393;\\\">◆新北市八仙粉塵氣爆專案帳戶 </span></strong><a href=\\\"http://www.sw.ntpc.gov.tw/web/News?command=showDetail&amp;postId=336389&amp;groupId=\\\" target=\\\"_blank\\\">Link</a></p>\\n<p>&nbsp;</p>\\n<p>戶名：新北市社會救濟會報專戶（英文為New Taipei City Social Assistance Account）</p>\\n<p>帳號：027038002803（台灣銀行板橋分行），需在空白處註名「八仙粉塵氣爆救助專案」。</p>\\n<p>&nbsp;</p>\\n<p>匯款後將匯款單傳真至新北市政府社會救助科 02-2966-6556，並於空白處加註捐款單位、地址及連絡電話，社會局收到捐款後，將開立收據給捐款單位。</p>\\n<p>&nbsp;</p>\\n<p>&nbsp;</p>\\n<p><strong><span style=\\\"color: #319393;\\\">◆陽光基金會燒傷生心理重建服務基金 </span></strong><a href=\\\"https://sunshine.eoffering.org.tw/contents/project_ct?p_id=22\\\" target=\\\"_blank\\\">Link</a></p>\\n<p>&nbsp;</p>\\n<p>戶名：財團法人陽光社會福利基金會</p>\\n<p>帳號： 007-144-50-713726（第一銀行城東分行)捐助『燒傷生心理重建服務』</p>\\n<p>&nbsp;</p>\\n<p>可透過郵政劃撥、銀行轉帳捐款、四大超商捐款、信用卡捐款單（僅陽光基金會提供）、線上捐款（僅陽光基金會提供）等五種方式捐款。</p>\\n<p>&nbsp;</p>\\n<p><span style=\\\"color: #ff0000;\\\">★</span><a href=\\\"http://udn.com/news/story/9/1025557\\\" target=\\\"_blank\\\">慷慨解囊！八仙塵爆企業名人捐款一覽</a></p>\\n<p>&nbsp;</p>\\n<p><strong>相關新聞:</strong></p>\\n<p><a href=\\\"http://udn.com/news/story/2/1025742\\\" target=\\\"_blank\\\">為何要捐給陽光基金會？網友回答好專業</a></p>\\n<p>&nbsp;</p>\\n<p><strong>2.捐血？基金會：血庫已夠用 請改定期捐血</strong></p>\\n<p>八仙樂園粉塵爆炸造成約500人輕重傷，很多民眾紛紛赴各捐血車、捐血中心捐血，希望盡一己之力。不過，台灣血液基金會29日表示，目前全台血漿庫存量供應無虞，而大台北地區雖收治燒傷患者最多，但目前急用的血漿供應還夠用，因血液有保存期限，因此，呼籲熱血民眾依照自己捐血時程、定期捐血。</p>\\n<p>&nbsp;</p>\\n<p><strong>外界提供的援助</strong></p>\\n<p><strong>1. 健保署:自費部分不向病人收費</strong></p>\\n<p>自6月27日至9月30日止傷者的自費醫療費用，全由健保署先行墊付，不向傷患收費，因本次事故傷者可持健保卡至特約院所，並主動表明病史，可優免自費醫療費用。</p>\\n<p><strong>2.醫美診所免費為塵爆傷者門診換藥</strong></p>\\n<p>台灣形體美容整合醫學會29日宣布，27家醫美診所提供八仙樂園塵爆燒燙傷患每天換藥完全免費，直到傷口癒合。後陸續又有多家診所響應此活動。</p>\\n<p><a href=\\\"http://udn.com/news/story/1/1022883\\\" target=\\\"_blank\\\">◆提供免費換藥診所查詢</a></p>\\n<p>&nbsp;&nbsp;</p>\\n<p><strong>3.全台45家旅館、宿舍 提供免費住宿：</strong></p>\\n<p>八仙事件，各界投入關懷，不少企業團體伸出援手，除了醫美診所提供免費換藥服務，北部、中部有45家旅館、學校宿舍提供給爆炸受傷家屬免費住宿，減輕家屬舟車勞頓與負擔。名單如下，也將會持續更新中。</p>\\n<p><a href=\\\"http://udn.com/news/story/2/1025343\\\" target=\\\"_blank\\\">◆哪些愛心旅館 詳細資訊點選</a></p>\\n<div id=\\\"JSyRaeZm6tk\\\" class=\\\"youtube video-container\\\"><iframe src=\\\"http://www.youtube.com/embed/JSyRaeZm6tk?controls=1&amp;disablekb=1&amp;modestbranding=1&amp;showinfo=0&amp;rel=0\\\" width=\\\"560\\\" height=\\\"315\\\" frameborder=\\\"0\\\" allowfullscreen=\\\"\\\"></iframe></div>\\n<div class=\\\"youtube video-container\\\">&nbsp;</div>\\n<div class=\\\"youtube video-container\\\">\\n<p><a href=\\\"http://udn.com/news/story/8313/1020812\\\" target=\\\"_blank\\\"><strong>【那一晚】現場像煉獄&hellip;火舌紋身 一拍皮就掉</strong></a></p>\\n<p>&nbsp;</p>\\n<p>&nbsp;</p>\\n<p>「現場簡直跟煉獄一樣」，十九歲的陳小姐腿部嚴重灼傷，她說事發當時和四名朋友就站在舞台正前方，突然一陣火從舞台竄起，火在瞬間就像蛇一樣，往舞台前人群大片竄燒，恐怖極了。</p>\\n<p>&nbsp;</p>\\n<p>陳女哭著說「我的腳都著火了，用手想拍掉，一塊皮就掉到地上」，「腳好痛，痛到根本沒有知覺，第一次跟朋友去參加party怎麼會這樣。」</p>\\n<p>&nbsp;</p>\\n<p>頭、手臂、雙腳都受傷的袁姓男子說，他當時與女友站在舞台前的正中央，DJ放著舞曲，大家都在嬉鬧，突然聽見尖叫聲，就看到舞台冒出超高的火舌，他拉著女友往後跑，女友跌倒，他跟女友頭部著火，全身被火舌吞噬，「差一點以為自己要死了」。</p>\\n<p><strong>相關新聞：</strong></p>\\n<p><a href=\\\"http://udn.com/news/story/2/1021594\\\" target=\\\"_blank\\\">和美鎮長女也燒傷：陌生人背我泡冰水</a></p>\\n<p><a href=\\\"http://udn.com/news/story/2/1021632\\\" target=\\\"_blank\\\">彤彤姐姐哭著打電話 目擊人成火球</a></p>\\n<p><a href=\\\"http://udn.com/news/story/8313/1020820-\\\" target=\\\"_blank\\\">粉塵爆炸像超音速 狠炸人群</a></p>\\n<p><a href=\\\"http://udn.com/news/story/8313/1021163\\\" target=\\\"_blank\\\">愛女90%燒燙傷 父痛哭：年輕生命這樣往生？</a></p>\\n<p>&nbsp;</p>\\n<p>&nbsp;</p>\\n<p>&nbsp;</p>\\n<p><a href=\\\"http://udn.com/news/story/8313/1021169\\\" target=\\\"_blank\\\"><strong>【投入的搶救】警消：救災27年 最慘最漫長一夜</strong></a></p>\\n<p>「從事警消工作27年，第一次遇到這麼大型的戶外氣爆案，現場數百人哀嚎聲不斷&hellip;」，水上樂園粉塵爆炸造成500多人灼傷，打破台灣救災史一次燒燙傷人數的最新紀錄，給警消上了一場震撼教育課，加上救災現場在遊樂園內，車輛迴轉空間不夠，加劇救災工作的困難度，而數百名警消、醫護、志工的全力，度過漫長的一夜。</p>\\n<p>&nbsp;</p>\\n<p><strong>相關新聞:</strong></p>\\n<p><a href=\\\"http://udn.com/news/story/8316/1022399\\\" target=\\\"_blank\\\">上千醫護 921後最大規模搶救</a></p>\\n<p><a href=\\\"http://udn.com/news/story/1/1022604\\\" target=\\\"_blank\\\">女醫師寫「塵爆急診那一夜」網友感動落淚</a></p>\\n<p><a href=\\\"http://udn.com/news/story/1/1022626\\\" target=\\\"_blank\\\">挨批拒收傷患 和信：有待命但沒人送來</a></p>\\n<p><a href=\\\"http://udn.com/news/story/1/1022528\\\" target=\\\"_blank\\\">醫院忙到「超頻」 洪浩雲暗批和信不收傷患</a></p>\\n<p>&nbsp;</p>\\n<p><img id=\\\"Sada0o9 \\\" style=\\\"max-width: 100% !important; height: auto; display: block;\\\" src=\\\"http://i.imgur.com/Sada0o9.jpg\\\" alt=\\\"\\\" /></p>\\n<h4>八仙樂園彩色派對主舞台昨晚粉塵氣爆，造成兩百多人灼傷，不少傷者皮開肉綻，現場血跡斑斑，救難人員讓傷患躺在游泳圈，避免傷口碰觸流血。 記者林昭彰／攝影</h4>\\n<p>&nbsp;</p>\\n<p><a href=\\\"http://udn.com/news/story/8313/1022430\\\" target=\\\"_blank\\\"><strong>【逃生的困難】人群擠在乾泳池灌粉 想逃得攀越池壁</strong></a></p>\\n<p>去年彩色派對地點選在海景池畔，今年卻改在抽乾的泳池裡；「臨時舞池」與地面高度有二公尺落差，，形同把人群集中在大窪地內噴粉，但意外發生，池壁卻成了影響逃生阻礙。。</p>\\n<p>&nbsp;</p>\\n<p>參與援救的消防隊員指出，即使風大，池底堆積的粉塵仍會被池壁阻擋，而沉積地板的粉塵越來越厚，才會導致粉塵密度過大，起火後連鎖燃爆。這可以想像一個擠滿人的凹地起火，人群如同困在火爐裡，想逃還得攀爬翻越池壁，這可能是造成重大傷亡的原因。</p>\\n<p><strong>相關新聞：</strong><a href=\\\"http://udn.com/news/story/8313/1024661\\\" target=\\\"_blank\\\">勘驗影片：鋼瓶滅火再爆燃 傷者二度火焚</a></p>\\n<p><img id=\\\"jtZHxVo \\\" style=\\\"max-width: 100% !important; height: auto; display: block;\\\" src=\\\"http://i.imgur.com/jtZHxVo.jpg\\\" alt=\\\"\\\" /></p>\\n<p><a href=\\\"http://udn.com/news/story/2/1021690\\\" target=\\\"_blank\\\"><strong>【傷亡情況】495人受傷161人病危10人死亡</strong></a> <a href=\\\"http://117.56.4.214/informationlist.aspx?uid=721\\\" target=\\\"_blank\\\">◆受傷名單查詢</a></p>\\n<p>八仙樂園塵爆意外傷害人數為495人，分別送至43家醫院。傷者多達4成是學生，共204名學生及1名老師受傷，外籍傷患六人，日籍、馬來西亞籍、新加坡籍、歐美人士，其他兩人尚未確認身分；大陸地區共七人，五位香港人、兩位大陸人士。。</p>\\n<p>&nbsp;</p>\\n<p>八仙塵爆傷者病情時常變化，<strong>目前還有343人分別在42家醫院做治療，其中213人在加護病房，162人病危(透析6 人、葉克膜0人、插管56人)，10人死亡。</strong></p>\\n<p>&nbsp;</p>\\n<p>截至目前為止，八仙氣爆共造成10人死亡，分別為中山附醫收治20歲女子李珮筠、台中榮總19歲男性劉致葦，以及林口長庚20歲男子陳天順、板橋亞東醫院23歲蘇家陞、台北慈濟醫院22歲黃小軒、基隆長庚醫院35歲曾芳津、台北馬偕醫院25歲陳孟宏、林口長庚醫院的19歲呂姓傷者、台南奇美醫學中心31歲郭姓女患者。</p>\\n<p>&nbsp;</p>\\n<p>&bull;6/29 第一死：20歲女傷者李珮筠經轉送台中中山醫學大學附設醫院搶救，在29日下午2時21分，宣告不治。</p>\\n<p>&nbsp;</p>\\n<p>&bull;7/02 第二死：台北海洋技術學院學生劉致葦全身95%燒燙傷，台中榮總醫療團隊全力治療，7月2日凌晨3時30分心室衰竭，醫護人員用電擊搶救，5時41分不治。</p>\\n<p>&nbsp;</p>\\n<p>&bull;7/06 第三者：淡江大學的20歲男學生陳天順全身50%灼傷，送醫後住加護病房，病情一度穩定。他受傷部位集中在手腳四肢，為深二度及深三度燒傷，合併吸入性灼傷，6日凌晨病情惡化，家屬忍痛決定，讓他回家後拔管，嚥下最後一口氣。</p>\\n<p>&nbsp;</p>\\n<p>&bull;7/10 第四死：台北醫學大學牙醫系3年級學生蘇家陞（23歲），全身約73%燒燙傷，送到亞東醫院急救，到院前廿分鐘還有意識，後來因腦部缺氧，且急救期間曾有十一分鐘心跳停止，緊急插管並加裝葉克膜救治，沒再恢復清醒，1日移除葉克膜、使用呼吸器。二日因腦水腫，經斷層掃瞄確認有迷漫性缺氧病變，導致腦神經壞死，造成腦幹功能喪失。八日研判有可能腦死狀態。家屬決定捐出心臟、肝臟等器官遺愛人間，醫護歷近10小時完成摘除，逾10人受惠。</p>\\n<p>&bull;7/12 第五死：22歲女性黃小軒全身90%二至三度燒傷，上個月27日送台北慈濟後，就因呼吸衰竭接受氣管插管，後來陸續接受緊急筋膜切開減壓手術、清創與植皮，搶救後一度穩定，但後來出現多重器官衰竭。經過搶救治療，12日上午7時28分仍傷重不治。</p>\\n<p>&nbsp;</p>\\n<p>&bull;7/15 第六死：曾芳津全身遭到57%燒燙傷，裝上葉克膜維持生命，因感染引發敗血症，導致全身多處器官衰竭，由於治療無效，7月15日下午拔管宣告不治。她的弟弟曾和健，也是八仙塵爆的受害者，燒燙傷面積達92%，正在台北榮總與死神搏鬥。</p>\\n<p>&nbsp;</p>\\n<p>&bull;7/16 第七死：就讀萬能科大的林芷妘，全身約有81%灼傷，一度因接受葉克膜治療，改善病情，不料昨天因感染導致敗血症，且併發多重器官衰竭，16日下午家屬決定拔管，不忍心讓她痛苦下去。</p>\\n<p>&nbsp;</p>\\n<p>&bull;7/20 第八死：陳孟宏受有體表面積55%的二至三級灼傷，併有吸入性肺部損傷，事發後送急診時就緊急插管，隨後轉入加護病房。雖然情況一度穩定，但近日病情急轉直下，經醫療團隊與家屬多次討論病情變化後，家屬不捨孩子承受過多無效醫療，同意終止所有醫療行為。</p>\\n<p>&nbsp;</p>\\n<p>&bull;7/23 第九死：19歲呂姓大學生全身65%燒燙傷，連日來在林口長庚醫院加護病房搶救，但因嚴重細菌感染，23日不幸死於敗血症，成為第九名不幸死亡的八仙意外傷者。</p>\\n<p>&nbsp;</p>\\n<p>&nbsp;</p>\\n<p>&bull;7/30 第十死：31歲郭姓女患者，全身有50%的燒燙傷、深度2至3度傷勢嚴重，住進奇美醫學中心燒燙傷加護病房救治，但經過連日來治療，仍因感染引發敗血性休克、合併多重器官衰竭以及腦幹功能喪失，於30日晚間7點06分宣告不治。</p>\\n<p>&nbsp;</p>\\n<p><strong>相關新聞:</strong></p>\\n<p><a href=\\\"http://udn.com/news/story/2/1025551\\\" target=\\\"_blank\\\">李珮筠不治 母不讓傷重弟知道</a></p>\\n<p><a href=\\\"http://udn.com/news/story/2/1025542\\\" target=\\\"_blank\\\">護理師：李珮筠傷勢嚴重到破表</a></p>\\n<p><a href=\\\"http://udn.com/news/story/8313/1030408\\\" target=\\\"_blank\\\">慟！八仙第2位罹難者 劉致葦走了</a></p>\\n<p><a href=\\\"http://udn.com/news/story/2/1025468\\\" target=\\\"_blank\\\">一開始的燒傷注定結局 醫師列死亡公式</a></p>\\n<p><a href=\\\"http://udn.com/news/story/8313/1029597\\\" target=\\\"_blank\\\">90%灼傷的呂意銘奇蹟脫險 激勵其他病患</a></p>\\n<p>&nbsp;</p>\\n<p><a href=\\\"http://udn.com/news/story/2/1090763\\\" target=\\\"_blank\\\">高雄長庚八仙傷患 第一人出院</a></p>\\n<p>&nbsp;</p>\\n<p><a href=\\\"http://udn.com/news/story/8313/1020811\\\" target=\\\"_blank\\\"><strong>【塵爆原因】有人抽菸？設備過熱？機器火花？</strong></a></p>\\n<p>「彩色派對」主舞台疑因玉米粉塵爆炸。消防人員初步研判，可能有人抽菸或舞台燈光溫度太高，大量粉塵著火後，產生連鎖反應而迅速燃燒引爆。新北市長朱立倫昨晚到場勘查後，要求業者立即停業接受調查。</p>\\n<p>&nbsp;</p>\\n<p>針對這起疑似粉塵燃爆意外，消防局火災調查人員初步調查，舞台以強風吹灑大量粉塵後，瞬間燃起火光引發爆炸；由於玉米粉塵粉末細微、具可燃性，若在噴灑時一遇有火源，熱量傳導到同時懸浮在周邊的細小顆粒，就會燃燒並產生連鎖反應，引發一團火球，像氣爆般的爆炸，讓人措手不及，因此研判可能是有人抽菸，或舞台燈光溫度太高，粉塵溫度到達燃點。</p>\\n<p>&nbsp;</p>\\n<p><strong>相關新聞：</strong></p>\\n<p><a href=\\\"http://udn.com/news/story/8313/1022428\\\" target=\\\"_blank\\\">滿地都是菸頭、打火機 消防局疑抽菸引爆</a></p>\\n<p><a href=\\\"http://udn.com/news/story/8313/1021146\\\" target=\\\"_blank\\\">檢警調查：粉塵濃度高 不排除混合性爆炸</a></p>\\n<p><img id=\\\"MBodnQg \\\" style=\\\"max-width: 100% !important; height: auto; display: block;\\\" src=\\\"http://i.imgur.com/MBodnQg.jpg\\\" alt=\\\"\\\" /></p>\\n<div class=\\\"photo_center\\\">\\n<h4>群眾搶著逃出爆炸現場，原本狂歡的主舞台搖滾區留下一地凌亂的衣物鞋子。 記者鄭清元／攝影</h4>\\n<a class=\\\"photo_pop_icon\\\" href=\\\"http://theme.udn.com/####\\\">分享</a>\\n<div class=\\\"photo_pop\\\">&nbsp;</div>\\n</div>\\n<p>&nbsp;</p>\\n<p><a href=\\\"http://udn.com/news/story/2/1021197\\\" target=\\\"_blank\\\"><strong>【活動內容】Color Play Asia彩色派對門票 一張售票1500元</strong></a></p>\\n<p>&nbsp;</p>\\n<p>主辦「Color Play Asia - 彩色派對」的玩色創意，2013年曾舉辦「西子灣彩色節音樂派對 Color Play」活動。</p>\\n<p>Color Play彩色派對是以印度的彩色節為藍本，並結合趣味娛樂和音樂饗宴，以決鬥的方式活動。就像大型的瘋狂派對，以彩粉互相拋擲攻擊，不論是相識不相識，到現場互相攻擊敵人或互相cover的夥伴。有點類似潑水節的概念。</p>\\n<p>&nbsp;</p>\\n<p>在八仙樂園舉辦的「Color Play Asia-彩色派對」以售票方式繽紛開戰，以「敢完、敢樂、最搖擺」為噱頭賣點，門票一張1500元，早鳥票1100元，若想要八仙樂園與彩色派對，一路暢遊到底的玩客，則必須花費將近新台幣2000元的票價。</p>\\n<p>&nbsp;</p>\\n<p><a href=\\\"http://udn.com/news/story/8313/1020821\\\" target=\\\"_blank\\\"><strong>【危險性】色彩炒話題 粉塵爆炸五大危機</strong></a></p>\\n<p><strong><img id=\\\"cwvI1zQ \\\" style=\\\"max-width: 100% !important; height: auto; display: block;\\\" src=\\\"http://i.imgur.com/cwvI1zQ.jpg\\\" alt=\\\"\\\" /></strong></p>\\n<p>&nbsp;</p>\\n<p>「粉塵氣爆五大元素」包括「點火源」或是高溫處，再來是「侷限空間」，雖然昨天八仙是開放戶外場地，但空氣不流通導致粉塵無處發散。再來是「充足氧氣」供粉塵燃燒，另外就是「散布」粉塵被大量的「撒」或「丟」與空氣接觸，最後是「可燃性粉塵」其中奶粉因含油脂更易燃燒。</p>\\n<p>&nbsp;</p>\\n<p><strong>相關新聞：</strong></p>\\n<p><a href=\\\"http://udn.com/news/story/7339/1022411\\\" target=\\\"_blank\\\">粉塵爆炸6個雷 八仙全踩了</a></p>\\n<p><a href=\\\"http://udn.com/news/story/8313/1022353\\\" target=\\\"_blank\\\">不只彩色趴 泡泡趴高危險</a></p>\\n<p>&nbsp;</p>\\n<p>&nbsp;</p>\\n<p><img id=\\\"AiJQ1L5 \\\" style=\\\"max-width: 100% !important; height: auto; display: block;\\\" src=\\\"http://i.imgur.com/AiJQ1L5.jpg\\\" alt=\\\"\\\" /></p>\\n<div class=\\\"photo_center\\\">\\n<h4>新北市八里的八仙樂園昨晚驚傳燃爆悲劇，造成逾500民眾燒燙傷送醫。 記者陳柏亨／攝影</h4>\\n<a class=\\\"photo_pop_icon\\\" href=\\\"http://theme.udn.com/####\\\">分享</a>\\n<div class=\\\"photo_pop\\\">&nbsp;</div>\\n</div>\\n<p><a href=\\\"http://udn.com/news/story/8313/1021164\\\" target=\\\"_blank\\\"><strong>【復健路】 醫憂：即使從國外調皮 也來不及</strong></a></p>\\n<p>&nbsp;</p>\\n<p>燒燙傷患者皮膚表面受傷必須植皮，使用人工皮、器捐來源皮膚或自體皮膚移植，來覆蓋燒燙傷的皮膚。醫師表示，最好的植皮來源是自體皮膚移植，取代壞死的皮膚；自體皮膚無法使用時，就要用器捐來源的屍皮，而屍皮較容易產生排斥問題。</p>\\n<p>藝人Selina因爆破戲意外，全身皮膚超過50%面積遭到燒傷，當時燒燙傷小組負責人、林口長庚整形外科系主任林志鴻表示，燒燙傷病人急需人工皮、自體皮膚移植、或屍皮等覆蓋燒傷的皮膚，除了幫助傷口癒合，也可預防感染等後遺症。</p>\\n<p><strong>相關新聞:</strong></p>\\n<p><a href=\\\"http://udn.com/news/story/2/1021497\\\" target=\\\"_blank\\\">陪伴燒傷病患應注意的事？陽光基金會列5點提醒</a></p>\\n<p><a href=\\\"http://udn.com/news/story/8316/1025433\\\" target=\\\"_blank\\\">需「皮」孔急 器捐中心籲捐「屍皮」</a></p>\\n<p><a href=\\\"http://udn.com/news/story/2/1025594\\\" target=\\\"_blank\\\">八仙塵爆燒燙傷 椰子水西瓜少吃</a></p>\\n<p>&nbsp;</p>\\n<p><img id=\\\"FkUPHMQ \\\" style=\\\"max-width: 100% !important; height: auto; display: block;\\\" src=\\\"http://i.imgur.com/FkUPHMQ.gif\\\" alt=\\\"\\\" /></p>\\n<p><a href=\\\"http://udn.com/news/story/2/1021337\\\" target=\\\"_blank\\\"><strong>【理賠狀況】公共意外責任險傷亡每人上限300萬 學生險最高可理賠100萬</strong></a></p>\\n<p>&nbsp;</p>\\n<p>八仙樂園彩色派對不幸發生粉塵爆炸意外，遭燒燙傷者不乏年輕學子，金管會表示，學生保險額度最高新台幣100萬元，只要是住院、手術等醫療事項都可申請理賠。</p>\\n<p>產險公會理事長戴英祥指出，八仙樂園公共意外責任險每一個人體傷新台幣500萬元、單一事件理賠金額上限是2000萬元。</p>\\n<p>彩虹派對1人是300萬元、單一事件理賠金額上限是3000萬元，因為事件受傷人數太多，因此兩個產險公共意外責任險的總理賠金額上限為5000萬元。</p>\\n<p>&nbsp;</p>\\n<p>&nbsp;</p>\\n<p><a href=\\\"http://udn.com/news/story/2/1021520\\\" target=\\\"_blank\\\"><strong>【偵辦情況】負責人100萬交保 網友肉搜：多次傳糾紛</strong></a></p>\\n<p><strong><img id=\\\"PFIDg7a \\\" style=\\\"max-width: 100% !important; height: auto; display: block;\\\" src=\\\"http://i.imgur.com/PFIDg7a.jpg\\\" alt=\\\"\\\" /></strong></p>\\n<p>&nbsp;</p>\\n<div class=\\\"photo_center\\\">\\n<h4>八仙樂園舉辦「彩虹派對」負責人呂忠吉(左)上午被移送士林地檢署。 記者蘇健忠／攝影</h4>\\n<a class=\\\"photo_pop_icon\\\" href=\\\"http://theme.udn.com/####\\\">分享</a>\\n<div class=\\\"photo_pop\\\">&nbsp;</div>\\n</div>\\n<p>八仙昨天發生粉塵爆炸意外，網友搜尋活動主辦人呂忠吉背景，發現他曾多次因為徵人、消費等糾紛，和網友槓上，過去就有網友列出呂忠吉種種爭議事蹟。</p>\\n<p>士林地檢署介入調查後，28日下午命主辦活動的「玩色創意」負責人呂忠吉(42歲)100萬元交保，限制出境、住居；負責硬體的邱柏銘、特效人員廖俊明30萬元交保、限制出境。現場噴灑彩色粉塵的工作人員盧建佑、沈浩然請回並限制住居。</p>\\n<p><strong>相關新聞：</strong></p>\\n<p><a href=\\\"http://udn.com/news/story/2/1021344\\\" target=\\\"_blank\\\">八仙樂園意外 網友指負責人爭議多</a></p>\\n<p><a href=\\\"http://udn.com/news/story/8313/1022434\\\" target=\\\"_blank\\\">五年掛六公司頭銜 呂開免洗公司？</a></p>\\n<p>&nbsp;</p>\\n<p><span style=\\\"color: #ff8000;\\\"><a href=\\\"http://udn.com/news/story/2/1020840\\\" target=\\\"_blank\\\"><strong>【誰該負責】八仙總經理：只租借場地 安全由派對業者負責</strong></a></span></p>\\n<p>八仙樂園總經理陳慧穎表示，八仙樂園只租借場地，合約載明安全保障由派對業者負責。派對門票是瑞博公司販售，瑞博有保險，八仙樂園也有投保意外險。等檢警釐清責任後，會來處理。</p>\\n<p>&nbsp;</p>\\n<p><strong>相關新聞：</strong></p>\\n<p><a href=\\\"http://udn.com/news/story/8313/1022324\\\" target=\\\"_blank\\\">萬海第三代陳慧穎 經營八仙26年來最大危機</a></p>\\n<p><a href=\\\"http://udn.com/news/story/8313/1025407\\\" target=\\\"_blank\\\">主持人彤彤、八仙總座遭約談</a></p>\\n<p>&nbsp;</p>\\n<p><a href=\\\"http://udn.com/news/story/8313/1021147\\\" target=\\\"_blank\\\"><strong>主題樂園大型活動 未來須核備</strong></a></p>\\n<p>八仙樂園是國內熱門主題樂園之一，粉塵爆炸事件發生後，遊樂園安全問題引發討論。交通部長陳建宇表示，主題樂園將場地借給公關公司辦活動，觀光局並不知情，未來將要求國內主題樂園辦理類似大型活動，都須向觀光局核備，保險理賠須經過查核，大型活動也必須納入年度評鑑考核。</p>\\n<p><img id=\\\"OOxshqA \\\" style=\\\"max-width: 100% !important; height: auto; display: block;\\\" src=\\\"http://i.imgur.com/OOxshqA.jpg\\\" alt=\\\"\\\" /></p>\\n</div><div id=\\\"pack_refrence\\\"><h1>引用資料</h1><h2>Youtube</h2><ol><li><a href=\\\"#\\\" onclick=\\\"window.open('http://www.youtube.com/watch?v=JSyRaeZm6tk', '_system');\\\">八仙樂園地獄火 12命危400傷</a></li></ol>\",\n" +
				"        \"bookmark\": [],\n" +
				"        \"file\": [\n" +
				"          \"AiJQ1L5.jpg\",\n" +
				"          \"cwvI1zQ.jpg\",\n" +
				"          \"FkUPHMQ.gif\",\n" +
				"          \"jtZHxVo.jpg\",\n" +
				"          \"MBodnQg.jpg\",\n" +
				"          \"OOxshqA.jpg\",\n" +
				"          \"PFIDg7a.jpg\",\n" +
				"          \"Sada0o9.jpg\",\n" +
				"          \"TIXHiEy.gif\"\n" +
				"        ],\n" +
				"        \"creator_user_name\": \"范振原\",\n" +
				"        \"is_public\": true,\n" +
				"        \"modified\": \"false\",\n" +
				"        \"private_id\": \"\",\n" +
				"        \"id\": \"version1439355907607\",\n" +
				"        \"view_count\": 6\n" +
				"      },\n" +
				"      {\n" +
				"        \"creator_user_id\": \"1009840175700426\",\n" +
				"        \"note\": [],\n" +
				"        \"create_time\": 1439358382000,\n" +
				"        \"pack_id\": \"pack1439355907459\",\n" +
				"        \"user_view_count\": 0,\n" +
				"        \"version\": 0,\n" +
				"        \"content\": \"<p><strong>時間：</strong>6月27日晚間約8點30分左右</p>\\n<p><strong>地點：</strong>新北市八仙樂園</p>\\n<p><strong>活動：</strong>玩色創意公司舉辦「Color Play Asia - 彩色派對」活動</p>\\n<p><strong>傷亡狀況：</strong>495人受傷，10人死亡（至7月23日為止有343人住院，在加護病房213名，病危162名）</p>\\n<p><strong>官方處理：</strong>新北市長朱立倫下令八仙樂園無限期停業。內政部宣布活動禁用可燃性微細粉末。</p>\\n<p><img id=\\\"TIXHiEy \\\" style=\\\"max-width: 100% !important; height: auto; display: block;\\\" src=\\\"FILE_STORAGE_PATHpack1439355907459/TIXHiEy.gif\\\" alt=\\\"\\\" /></p>\\n<p>&nbsp;</p>\\n<p><strong>民眾怎麼幫助爆炸傷者？</strong></p>\\n<p><strong>1.捐款</strong></p>\\n<p>截至目前為止，新北市愛心專戶收到10萬2144筆捐款、累計10億8680萬9046元。12日召開八仙粉塵氣爆案受害家屬代表會議，共有23位各大醫院受害家屬推派的代表出席，會中推選出7人成為善款專戶管理委員會委員。</p>\\n<p>&nbsp;</p>\\n<p><strong><span style=\\\"color: #319393;\\\">◆新北市八仙粉塵氣爆專案帳戶 </span></strong><a href=\\\"http://www.sw.ntpc.gov.tw/web/News?command=showDetail&amp;postId=336389&amp;groupId=\\\" target=\\\"_blank\\\">Link</a></p>\\n<p>&nbsp;</p>\\n<p>戶名：新北市社會救濟會報專戶（英文為New Taipei City Social Assistance Account）</p>\\n<p>帳號：027038002803（台灣銀行板橋分行），需在空白處註名「八仙粉塵氣爆救助專案」。</p>\\n<p>&nbsp;</p>\\n<p>匯款後將匯款單傳真至新北市政府社會救助科 02-2966-6556，並於空白處加註捐款單位、地址及連絡電話，社會局收到捐款後，將開立收據給捐款單位。</p>\\n<p>&nbsp;</p>\\n<p>&nbsp;</p>\\n<p><strong><span style=\\\"color: #319393;\\\">◆陽光基金會燒傷生心理重建服務基金 </span></strong><a href=\\\"https://sunshine.eoffering.org.tw/contents/project_ct?p_id=22\\\" target=\\\"_blank\\\">Link</a></p>\\n<p>&nbsp;</p>\\n<p>戶名：財團法人陽光社會福利基金會</p>\\n<p>帳號： 007-144-50-713726（第一銀行城東分行)捐助『燒傷生心理重建服務』</p>\\n<p>&nbsp;</p>\\n<p>可透過郵政劃撥、銀行轉帳捐款、四大超商捐款、信用卡捐款單（僅陽光基金會提供）、線上捐款（僅陽光基金會提供）等五種方式捐款。</p>\\n<p>&nbsp;</p>\\n<p><span style=\\\"color: #ff0000;\\\">★</span><a href=\\\"http://udn.com/news/story/9/1025557\\\" target=\\\"_blank\\\">慷慨解囊！八仙塵爆企業名人捐款一覽</a></p>\\n<p>&nbsp;</p>\\n<p><strong>相關新聞:</strong></p>\\n<p><a href=\\\"http://udn.com/news/story/2/1025742\\\" target=\\\"_blank\\\">為何要捐給陽光基金會？網友回答好專業</a></p>\\n<p>&nbsp;</p>\\n<p><strong>2.捐血？基金會：血庫已夠用 請改定期捐血</strong></p>\\n<p>八仙樂園粉塵爆炸造成約500人輕重傷，很多民眾紛紛赴各捐血車、捐血中心捐血，希望盡一己之力。不過，台灣血液基金會29日表示，目前全台血漿庫存量供應無虞，而大台北地區雖收治燒傷患者最多，但目前急用的血漿供應還夠用，因血液有保存期限，因此，呼籲熱血民眾依照自己捐血時程、定期捐血。</p>\\n<p>&nbsp;</p>\\n<p><strong>外界提供的援助</strong></p>\\n<p><strong>1. 健保署:自費部分不向病人收費</strong></p>\\n<p>自6月27日至9月30日止傷者的自費醫療費用，全由健保署先行墊付，不向傷患收費，因本次事故傷者可持健保卡至特約院所，並主動表明病史，可優免自費醫療費用。</p>\\n<p><strong>2.醫美診所免費為塵爆傷者門診換藥</strong></p>\\n<p>台灣形體美容整合醫學會29日宣布，27家醫美診所提供八仙樂園塵爆燒燙傷患每天換藥完全免費，直到傷口癒合。後陸續又有多家診所響應此活動。</p>\\n<p><a href=\\\"http://udn.com/news/story/1/1022883\\\" target=\\\"_blank\\\">◆提供免費換藥診所查詢</a></p>\\n<p>&nbsp;&nbsp;</p>\\n<p><strong>3.全台45家旅館、宿舍 提供免費住宿：</strong></p>\\n<p>八仙事件，各界投入關懷，不少企業團體伸出援手，除了醫美診所提供免費換藥服務，北部、中部有45家旅館、學校宿舍提供給爆炸受傷家屬免費住宿，減輕家屬舟車勞頓與負擔。名單如下，也將會持續更新中。</p>\\n<p><a href=\\\"http://udn.com/news/story/2/1025343\\\" target=\\\"_blank\\\">◆哪些愛心旅館 詳細資訊點選</a></p>\\n<div id=\\\"JSyRaeZm6tk\\\" class=\\\"youtube video-container\\\"><iframe src=\\\"http://www.youtube.com/embed/JSyRaeZm6tk?controls=1&amp;disablekb=1&amp;modestbranding=1&amp;showinfo=0&amp;rel=0\\\" width=\\\"560\\\" height=\\\"315\\\" frameborder=\\\"0\\\" allowfullscreen=\\\"\\\"></iframe></div>\\n<p><a href=\\\"http://udn.com/news/story/8313/1020812\\\" target=\\\"_blank\\\"><strong>【那一晚】現場像煉獄&hellip;火舌紋身 一拍皮就掉</strong></a></p>\\n<p>&nbsp;</p>\\n<p>&nbsp;</p>\\n<p>「現場簡直跟煉獄一樣」，十九歲的陳小姐腿部嚴重灼傷，她說事發當時和四名朋友就站在舞台正前方，突然一陣火從舞台竄起，火在瞬間就像蛇一樣，往舞台前人群大片竄燒，恐怖極了。</p>\\n<p>&nbsp;</p>\\n<p>陳女哭著說「我的腳都著火了，用手想拍掉，一塊皮就掉到地上」，「腳好痛，痛到根本沒有知覺，第一次跟朋友去參加party怎麼會這樣。」</p>\\n<p>&nbsp;</p>\\n<p>頭、手臂、雙腳都受傷的袁姓男子說，他當時與女友站在舞台前的正中央，DJ放著舞曲，大家都在嬉鬧，突然聽見尖叫聲，就看到舞台冒出超高的火舌，他拉著女友往後跑，女友跌倒，他跟女友頭部著火，全身被火舌吞噬，「差一點以為自己要死了」。</p>\\n<p><strong>相關新聞：</strong></p>\\n<p><a href=\\\"http://udn.com/news/story/2/1021594\\\" target=\\\"_blank\\\">和美鎮長女也燒傷：陌生人背我泡冰水</a></p>\\n<p><a href=\\\"http://udn.com/news/story/2/1021632\\\" target=\\\"_blank\\\">彤彤姐姐哭著打電話 目擊人成火球</a></p>\\n<p><a href=\\\"http://udn.com/news/story/8313/1020820-\\\" target=\\\"_blank\\\">粉塵爆炸像超音速 狠炸人群</a></p>\\n<p><a href=\\\"http://udn.com/news/story/8313/1021163\\\" target=\\\"_blank\\\">愛女90%燒燙傷 父痛哭：年輕生命這樣往生？</a></p>\\n<p>&nbsp;</p>\\n<p>&nbsp;</p>\\n<p>&nbsp;</p>\\n<p><a href=\\\"http://udn.com/news/story/8313/1021169\\\" target=\\\"_blank\\\"><strong>【投入的搶救】警消：救災27年 最慘最漫長一夜</strong></a></p>\\n<p>「從事警消工作27年，第一次遇到這麼大型的戶外氣爆案，現場數百人哀嚎聲不斷&hellip;」，水上樂園粉塵爆炸造成500多人灼傷，打破台灣救災史一次燒燙傷人數的最新紀錄，給警消上了一場震撼教育課，加上救災現場在遊樂園內，車輛迴轉空間不夠，加劇救災工作的困難度，而數百名警消、醫護、志工的全力，度過漫長的一夜。</p>\\n<p>&nbsp;</p>\\n<p><strong>相關新聞:</strong></p>\\n<p><a href=\\\"http://udn.com/news/story/8316/1022399\\\" target=\\\"_blank\\\">上千醫護 921後最大規模搶救</a></p>\\n<p><a href=\\\"http://udn.com/news/story/1/1022604\\\" target=\\\"_blank\\\">女醫師寫「塵爆急診那一夜」網友感動落淚</a></p>\\n<p><a href=\\\"http://udn.com/news/story/1/1022626\\\" target=\\\"_blank\\\">挨批拒收傷患 和信：有待命但沒人送來</a></p>\\n<p><a href=\\\"http://udn.com/news/story/1/1022528\\\" target=\\\"_blank\\\">醫院忙到「超頻」 洪浩雲暗批和信不收傷患</a></p>\\n<p>&nbsp;</p>\\n<p><img id=\\\"Sada0o9 \\\" style=\\\"max-width: 100% !important; height: auto; display: block;\\\" src=\\\"http://i.imgur.com/Sada0o9.jpg\\\" alt=\\\"\\\" /></p>\\n<h4>八仙樂園彩色派對主舞台昨晚粉塵氣爆，造成兩百多人灼傷，不少傷者皮開肉綻，現場血跡斑斑，救難人員讓傷患躺在游泳圈，避免傷口碰觸流血。 記者林昭彰／攝影</h4>\\n<p>&nbsp;</p>\\n<p><a href=\\\"http://udn.com/news/story/8313/1022430\\\" target=\\\"_blank\\\"><strong>【逃生的困難】人群擠在乾泳池灌粉 想逃得攀越池壁</strong></a></p>\\n<p>去年彩色派對地點選在海景池畔，今年卻改在抽乾的泳池裡；「臨時舞池」與地面高度有二公尺落差，，形同把人群集中在大窪地內噴粉，但意外發生，池壁卻成了影響逃生阻礙。。</p>\\n<p>&nbsp;</p>\\n<p>參與援救的消防隊員指出，即使風大，池底堆積的粉塵仍會被池壁阻擋，而沉積地板的粉塵越來越厚，才會導致粉塵密度過大，起火後連鎖燃爆。這可以想像一個擠滿人的凹地起火，人群如同困在火爐裡，想逃還得攀爬翻越池壁，這可能是造成重大傷亡的原因。</p>\\n<p><strong>相關新聞：</strong><a href=\\\"http://udn.com/news/story/8313/1024661\\\" target=\\\"_blank\\\">勘驗影片：鋼瓶滅火再爆燃 傷者二度火焚</a></p>\\n<p><img id=\\\"jtZHxVo \\\" style=\\\"max-width: 100% !important; height: auto; display: block;\\\" src=\\\"http://i.imgur.com/jtZHxVo.jpg\\\" alt=\\\"\\\" /></p>\\n<p><a href=\\\"http://udn.com/news/story/2/1021690\\\" target=\\\"_blank\\\"><strong>【傷亡情況】495人受傷161人病危10人死亡</strong></a> <a href=\\\"http://117.56.4.214/informationlist.aspx?uid=721\\\" target=\\\"_blank\\\">◆受傷名單查詢</a></p>\\n<p>八仙樂園塵爆意外傷害人數為495人，分別送至43家醫院。傷者多達4成是學生，共204名學生及1名老師受傷，外籍傷患六人，日籍、馬來西亞籍、新加坡籍、歐美人士，其他兩人尚未確認身分；大陸地區共七人，五位香港人、兩位大陸人士。。</p>\\n<p>&nbsp;</p>\\n<p>八仙塵爆傷者病情時常變化，<strong>目前還有343人分別在42家醫院做治療，其中213人在加護病房，162人病危(透析6 人、葉克膜0人、插管56人)，10人死亡。</strong></p>\\n<p>&nbsp;</p>\\n<p>截至目前為止，八仙氣爆共造成10人死亡，分別為中山附醫收治20歲女子李珮筠、台中榮總19歲男性劉致葦，以及林口長庚20歲男子陳天順、板橋亞東醫院23歲蘇家陞、台北慈濟醫院22歲黃小軒、基隆長庚醫院35歲曾芳津、台北馬偕醫院25歲陳孟宏、林口長庚醫院的19歲呂姓傷者、台南奇美醫學中心31歲郭姓女患者。</p>\\n<p>&nbsp;</p>\\n<p>&bull;6/29 第一死：20歲女傷者李珮筠經轉送台中中山醫學大學附設醫院搶救，在29日下午2時21分，宣告不治。</p>\\n<p>&nbsp;</p>\\n<p>&bull;7/02 第二死：台北海洋技術學院學生劉致葦全身95%燒燙傷，台中榮總醫療團隊全力治療，7月2日凌晨3時30分心室衰竭，醫護人員用電擊搶救，5時41分不治。</p>\\n<p>&nbsp;</p>\\n<p>&bull;7/06 第三者：淡江大學的20歲男學生陳天順全身50%灼傷，送醫後住加護病房，病情一度穩定。他受傷部位集中在手腳四肢，為深二度及深三度燒傷，合併吸入性灼傷，6日凌晨病情惡化，家屬忍痛決定，讓他回家後拔管，嚥下最後一口氣。</p>\\n<p>&nbsp;</p>\\n<p>&bull;7/10 第四死：台北醫學大學牙醫系3年級學生蘇家陞（23歲），全身約73%燒燙傷，送到亞東醫院急救，到院前廿分鐘還有意識，後來因腦部缺氧，且急救期間曾有十一分鐘心跳停止，緊急插管並加裝葉克膜救治，沒再恢復清醒，1日移除葉克膜、使用呼吸器。二日因腦水腫，經斷層掃瞄確認有迷漫性缺氧病變，導致腦神經壞死，造成腦幹功能喪失。八日研判有可能腦死狀態。家屬決定捐出心臟、肝臟等器官遺愛人間，醫護歷近10小時完成摘除，逾10人受惠。</p>\\n<p>&bull;7/12 第五死：22歲女性黃小軒全身90%二至三度燒傷，上個月27日送台北慈濟後，就因呼吸衰竭接受氣管插管，後來陸續接受緊急筋膜切開減壓手術、清創與植皮，搶救後一度穩定，但後來出現多重器官衰竭。經過搶救治療，12日上午7時28分仍傷重不治。</p>\\n<p>&nbsp;</p>\\n<p>&bull;7/15 第六死：曾芳津全身遭到57%燒燙傷，裝上葉克膜維持生命，因感染引發敗血症，導致全身多處器官衰竭，由於治療無效，7月15日下午拔管宣告不治。她的弟弟曾和健，也是八仙塵爆的受害者，燒燙傷面積達92%，正在台北榮總與死神搏鬥。</p>\\n<p>&nbsp;</p>\\n<p>&bull;7/16 第七死：就讀萬能科大的林芷妘，全身約有81%灼傷，一度因接受葉克膜治療，改善病情，不料昨天因感染導致敗血症，且併發多重器官衰竭，16日下午家屬決定拔管，不忍心讓她痛苦下去。</p>\\n<p>&nbsp;</p>\\n<p>&bull;7/20 第八死：陳孟宏受有體表面積55%的二至三級灼傷，併有吸入性肺部損傷，事發後送急診時就緊急插管，隨後轉入加護病房。雖然情況一度穩定，但近日病情急轉直下，經醫療團隊與家屬多次討論病情變化後，家屬不捨孩子承受過多無效醫療，同意終止所有醫療行為。</p>\\n<p>&nbsp;</p>\\n<p>&bull;7/23 第九死：19歲呂姓大學生全身65%燒燙傷，連日來在林口長庚醫院加護病房搶救，但因嚴重細菌感染，23日不幸死於敗血症，成為第九名不幸死亡的八仙意外傷者。</p>\\n<p>&nbsp;</p>\\n<p>&nbsp;</p>\\n<p>&bull;7/30 第十死：31歲郭姓女患者，全身有50%的燒燙傷、深度2至3度傷勢嚴重，住進奇美醫學中心燒燙傷加護病房救治，但經過連日來治療，仍因感染引發敗血性休克、合併多重器官衰竭以及腦幹功能喪失，於30日晚間7點06分宣告不治。</p>\\n<p>&nbsp;</p>\\n<p><strong>相關新聞:</strong></p>\\n<p><a href=\\\"http://udn.com/news/story/2/1025551\\\" target=\\\"_blank\\\">李珮筠不治 母不讓傷重弟知道</a></p>\\n<p><a href=\\\"http://udn.com/news/story/2/1025542\\\" target=\\\"_blank\\\">護理師：李珮筠傷勢嚴重到破表</a></p>\\n<p><a href=\\\"http://udn.com/news/story/8313/1030408\\\" target=\\\"_blank\\\">慟！八仙第2位罹難者 劉致葦走了</a></p>\\n<p><a href=\\\"http://udn.com/news/story/2/1025468\\\" target=\\\"_blank\\\">一開始的燒傷注定結局 醫師列死亡公式</a></p>\\n<p><a href=\\\"http://udn.com/news/story/8313/1029597\\\" target=\\\"_blank\\\">90%灼傷的呂意銘奇蹟脫險 激勵其他病患</a></p>\\n<p>&nbsp;</p>\\n<p><a href=\\\"http://udn.com/news/story/2/1090763\\\" target=\\\"_blank\\\">高雄長庚八仙傷患 第一人出院</a></p>\\n<p>&nbsp;</p>\\n<p><a href=\\\"http://udn.com/news/story/8313/1020811\\\" target=\\\"_blank\\\"><strong>【塵爆原因】有人抽菸？設備過熱？機器火花？</strong></a></p>\\n<p>「彩色派對」主舞台疑因玉米粉塵爆炸。消防人員初步研判，可能有人抽菸或舞台燈光溫度太高，大量粉塵著火後，產生連鎖反應而迅速燃燒引爆。新北市長朱立倫昨晚到場勘查後，要求業者立即停業接受調查。</p>\\n<p>&nbsp;</p>\\n<p>針對這起疑似粉塵燃爆意外，消防局火災調查人員初步調查，舞台以強風吹灑大量粉塵後，瞬間燃起火光引發爆炸；由於玉米粉塵粉末細微、具可燃性，若在噴灑時一遇有火源，熱量傳導到同時懸浮在周邊的細小顆粒，就會燃燒並產生連鎖反應，引發一團火球，像氣爆般的爆炸，讓人措手不及，因此研判可能是有人抽菸，或舞台燈光溫度太高，粉塵溫度到達燃點。</p>\\n<p>&nbsp;</p>\\n<p><strong>相關新聞：</strong></p>\\n<p><a href=\\\"http://udn.com/news/story/8313/1022428\\\" target=\\\"_blank\\\">滿地都是菸頭、打火機 消防局疑抽菸引爆</a></p>\\n<p><a href=\\\"http://udn.com/news/story/8313/1021146\\\" target=\\\"_blank\\\">檢警調查：粉塵濃度高 不排除混合性爆炸</a></p>\\n<p><img id=\\\"MBodnQg \\\" style=\\\"max-width: 100% !important; height: auto; display: block;\\\" src=\\\"http://i.imgur.com/MBodnQg.jpg\\\" alt=\\\"\\\" /></p>\\n<div class=\\\"photo_center\\\">\\n<h4>群眾搶著逃出爆炸現場，原本狂歡的主舞台搖滾區留下一地凌亂的衣物鞋子。 記者鄭清元／攝影</h4>\\n<a class=\\\"photo_pop_icon\\\" href=\\\"http://theme.udn.com/####\\\">分享</a>\\n<div class=\\\"photo_pop\\\">&nbsp;</div>\\n</div>\\n<p>&nbsp;</p>\\n<p><a href=\\\"http://udn.com/news/story/2/1021197\\\" target=\\\"_blank\\\"><strong>【活動內容】Color Play Asia彩色派對門票 一張售票1500元</strong></a></p>\\n<p>&nbsp;</p>\\n<p>主辦「Color Play Asia - 彩色派對」的玩色創意，2013年曾舉辦「西子灣彩色節音樂派對 Color Play」活動。</p>\\n<p>Color Play彩色派對是以印度的彩色節為藍本，並結合趣味娛樂和音樂饗宴，以決鬥的方式活動。就像大型的瘋狂派對，以彩粉互相拋擲攻擊，不論是相識不相識，到現場互相攻擊敵人或互相cover的夥伴。有點類似潑水節的概念。</p>\\n<p>&nbsp;</p>\\n<p>在八仙樂園舉辦的「Color Play Asia-彩色派對」以售票方式繽紛開戰，以「敢完、敢樂、最搖擺」為噱頭賣點，門票一張1500元，早鳥票1100元，若想要八仙樂園與彩色派對，一路暢遊到底的玩客，則必須花費將近新台幣2000元的票價。</p>\\n<p>&nbsp;</p>\\n<p><a href=\\\"http://udn.com/news/story/8313/1020821\\\" target=\\\"_blank\\\"><strong>【危險性】色彩炒話題 粉塵爆炸五大危機</strong></a></p>\\n<p><strong><img id=\\\"cwvI1zQ \\\" style=\\\"max-width: 100% !important; height: auto; display: block;\\\" src=\\\"http://i.imgur.com/cwvI1zQ.jpg\\\" alt=\\\"\\\" /></strong></p>\\n<p>&nbsp;</p>\\n<p>「粉塵氣爆五大元素」包括「點火源」或是高溫處，再來是「侷限空間」，雖然昨天八仙是開放戶外場地，但空氣不流通導致粉塵無處發散。再來是「充足氧氣」供粉塵燃燒，另外就是「散布」粉塵被大量的「撒」或「丟」與空氣接觸，最後是「可燃性粉塵」其中奶粉因含油脂更易燃燒。</p>\\n<p>&nbsp;</p>\\n<p><strong>相關新聞：</strong></p>\\n<p><a href=\\\"http://udn.com/news/story/7339/1022411\\\" target=\\\"_blank\\\">粉塵爆炸6個雷 八仙全踩了</a></p>\\n<p><a href=\\\"http://udn.com/news/story/8313/1022353\\\" target=\\\"_blank\\\">不只彩色趴 泡泡趴高危險</a></p>\\n<p>&nbsp;</p>\\n<p>&nbsp;</p>\\n<p><img id=\\\"AiJQ1L5 \\\" style=\\\"max-width: 100% !important; height: auto; display: block;\\\" src=\\\"http://i.imgur.com/AiJQ1L5.jpg\\\" alt=\\\"\\\" /></p>\\n<div class=\\\"photo_center\\\">\\n<h4>新北市八里的八仙樂園昨晚驚傳燃爆悲劇，造成逾500民眾燒燙傷送醫。 記者陳柏亨／攝影</h4>\\n<a class=\\\"photo_pop_icon\\\" href=\\\"http://theme.udn.com/####\\\">分享</a>\\n<div class=\\\"photo_pop\\\">&nbsp;</div>\\n</div>\\n<p><a href=\\\"http://udn.com/news/story/8313/1021164\\\" target=\\\"_blank\\\"><strong>【復健路】 醫憂：即使從國外調皮 也來不及</strong></a></p>\\n<p>&nbsp;</p>\\n<p>燒燙傷患者皮膚表面受傷必須植皮，使用人工皮、器捐來源皮膚或自體皮膚移植，來覆蓋燒燙傷的皮膚。醫師表示，最好的植皮來源是自體皮膚移植，取代壞死的皮膚；自體皮膚無法使用時，就要用器捐來源的屍皮，而屍皮較容易產生排斥問題。</p>\\n<p>藝人Selina因爆破戲意外，全身皮膚超過50%面積遭到燒傷，當時燒燙傷小組負責人、林口長庚整形外科系主任林志鴻表示，燒燙傷病人急需人工皮、自體皮膚移植、或屍皮等覆蓋燒傷的皮膚，除了幫助傷口癒合，也可預防感染等後遺症。</p>\\n<p><strong>相關新聞:</strong></p>\\n<p><a href=\\\"http://udn.com/news/story/2/1021497\\\" target=\\\"_blank\\\">陪伴燒傷病患應注意的事？陽光基金會列5點提醒</a></p>\\n<p><a href=\\\"http://udn.com/news/story/8316/1025433\\\" target=\\\"_blank\\\">需「皮」孔急 器捐中心籲捐「屍皮」</a></p>\\n<p><a href=\\\"http://udn.com/news/story/2/1025594\\\" target=\\\"_blank\\\">八仙塵爆燒燙傷 椰子水西瓜少吃</a></p>\\n<p>&nbsp;</p>\\n<p><img id=\\\"FkUPHMQ \\\" style=\\\"max-width: 100% !important; height: auto; display: block;\\\" src=\\\"http://i.imgur.com/FkUPHMQ.gif\\\" alt=\\\"\\\" /></p>\\n<p><a href=\\\"http://udn.com/news/story/2/1021337\\\" target=\\\"_blank\\\"><strong>【理賠狀況】公共意外責任險傷亡每人上限300萬 學生險最高可理賠100萬</strong></a></p>\\n<p>&nbsp;</p>\\n<p>八仙樂園彩色派對不幸發生粉塵爆炸意外，遭燒燙傷者不乏年輕學子，金管會表示，學生保險額度最高新台幣100萬元，只要是住院、手術等醫療事項都可申請理賠。</p>\\n<p>產險公會理事長戴英祥指出，八仙樂園公共意外責任險每一個人體傷新台幣500萬元、單一事件理賠金額上限是2000萬元。</p>\\n<p>彩虹派對1人是300萬元、單一事件理賠金額上限是3000萬元，因為事件受傷人數太多，因此兩個產險公共意外責任險的總理賠金額上限為5000萬元。</p>\\n<p>&nbsp;</p>\\n<p>&nbsp;</p>\\n<p><a href=\\\"http://udn.com/news/story/2/1021520\\\" target=\\\"_blank\\\"><strong>【偵辦情況】負責人100萬交保 網友肉搜：多次傳糾紛</strong></a></p>\\n<p><strong><img id=\\\"PFIDg7a \\\" style=\\\"max-width: 100% !important; height: auto; display: block;\\\" src=\\\"http://i.imgur.com/PFIDg7a.jpg\\\" alt=\\\"\\\" /></strong></p>\\n<p>&nbsp;</p>\\n<div class=\\\"photo_center\\\">\\n<h4>八仙樂園舉辦「彩虹派對」負責人呂忠吉(左)上午被移送士林地檢署。 記者蘇健忠／攝影</h4>\\n<a class=\\\"photo_pop_icon\\\" href=\\\"http://theme.udn.com/####\\\">分享</a>\\n<div class=\\\"photo_pop\\\">&nbsp;</div>\\n</div>\\n<p>八仙昨天發生粉塵爆炸意外，網友搜尋活動主辦人呂忠吉背景，發現他曾多次因為徵人、消費等糾紛，和網友槓上，過去就有網友列出呂忠吉種種爭議事蹟。</p>\\n<p>士林地檢署介入調查後，28日下午命主辦活動的「玩色創意」負責人呂忠吉(42歲)100萬元交保，限制出境、住居；負責硬體的邱柏銘、特效人員廖俊明30萬元交保、限制出境。現場噴灑彩色粉塵的工作人員盧建佑、沈浩然請回並限制住居。</p>\\n<p><strong>相關新聞：</strong></p>\\n<p><a href=\\\"http://udn.com/news/story/2/1021344\\\" target=\\\"_blank\\\">八仙樂園意外 網友指負責人爭議多</a></p>\\n<p><a href=\\\"http://udn.com/news/story/8313/1022434\\\" target=\\\"_blank\\\">五年掛六公司頭銜 呂開免洗公司？</a></p>\\n<p>&nbsp;</p>\\n<p><span style=\\\"color: #ff8000;\\\"><a href=\\\"http://udn.com/news/story/2/1020840\\\" target=\\\"_blank\\\"><strong>【誰該負責】八仙總經理：只租借場地 安全由派對業者負責</strong></a></span></p>\\n<p>八仙樂園總經理陳慧穎表示，八仙樂園只租借場地，合約載明安全保障由派對業者負責。派對門票是瑞博公司販售，瑞博有保險，八仙樂園也有投保意外險。等檢警釐清責任後，會來處理。</p>\\n<p>&nbsp;</p>\\n<p><strong>相關新聞：</strong></p>\\n<p><a href=\\\"http://udn.com/news/story/8313/1022324\\\" target=\\\"_blank\\\">萬海第三代陳慧穎 經營八仙26年來最大危機</a></p>\\n<p><a href=\\\"http://udn.com/news/story/8313/1025407\\\" target=\\\"_blank\\\">主持人彤彤、八仙總座遭約談</a></p>\\n<p>&nbsp;</p>\\n<p><a href=\\\"http://udn.com/news/story/8313/1021147\\\" target=\\\"_blank\\\"><strong>主題樂園大型活動 未來須核備</strong></a></p>\\n<p>八仙樂園是國內熱門主題樂園之一，粉塵爆炸事件發生後，遊樂園安全問題引發討論。交通部長陳建宇表示，主題樂園將場地借給公關公司辦活動，觀光局並不知情，未來將要求國內主題樂園辦理類似大型活動，都須向觀光局核備，保險理賠須經過查核，大型活動也必須納入年度評鑑考核。</p>\\n<p><img id=\\\"OOxshqA \\\" style=\\\"max-width: 100% !important; height: auto; display: block;\\\" src=\\\"http://i.imgur.com/OOxshqA.jpg\\\" alt=\\\"\\\" /></p><div id=\\\"pack_refrence\\\"><h1>引用資料</h1><h2>Youtube</h2><ol><li><a href=\\\"#\\\" onclick=\\\"window.open('http://www.youtube.com/watch?v=JSyRaeZm6tk', '_system');\\\">八仙樂園地獄火 12命危400傷</a></li></ol>\",\n" +
				"        \"bookmark\": [],\n" +
				"        \"file\": [],\n" +
				"        \"creator_user_name\": \"范振原\",\n" +
				"        \"is_public\": true,\n" +
				"        \"modified\": \"false\",\n" +
				"        \"private_id\": \"\",\n" +
				"        \"id\": \"version1439357056319\",\n" +
				"        \"view_count\": 12\n" +
				"      }\n" +
				"    ],\n" +
				"    \"tags\": \"\"\n" +
				"  },\n" +
				"  \"pack1439367500493\": {\n" +
				"    \"creator_user_id\": \"10204250001235141\",\n" +
				"    \"cover_filename\": \"F2BqN25.jpg\",\n" +
				"    \"create_time\": 1439367500000,\n" +
				"    \"creator_user_name\": \"莊晏\",\n" +
				"    \"name\": \"號外號外~4G方案大解析\",\n" +
				"    \"is_public\": true,\n" +
				"    \"description\": \"4G\",\n" +
				"    \"version\": [\n" +
				"      {\n" +
				"        \"creator_user_id\": \"10204250001235141\",\n" +
				"        \"note\": [],\n" +
				"        \"create_time\": 1439367500000,\n" +
				"        \"pack_id\": \"pack1439367500493\",\n" +
				"        \"user_view_count\": 0,\n" +
				"        \"version\": 0,\n" +
				"        \"content\": \"<h1 style=\\\"color: blue;\\\"><span style=\\\"background-color: #ccffff;\\\"><em><strong>1.中華電信 4G 987學生攜碼:&nbsp;</strong></em></span></h1>\\n<div>中華電信千元有找方案七月份只剩987學生攜碼吃到飽方案，且嚴格限制必須具備合格學生資格(國中~大專以上)才能申辦。</div>\\n<div>若不具學生資格又非種花不可的人，且只能多花月租212元申辦中華電信新推出的1199平板大玩專案。</div>\\n<div>&nbsp;</div>\\n<div>&nbsp;</div>\\n<div><span style=\\\"background-color: #ccffff;\\\"><em><strong><img id=\\\"lgbsg6A \\\" style=\\\"max-width: 100% !important; height: auto; display: block;\\\" src=\\\"FILE_STORAGE_PATHpack1439367500493/lgbsg6A.jpg\\\" alt=\\\"\\\" /></strong></em></span></div>\\n<div>&nbsp;</div>\\n<div>&nbsp;</div>\\n<div>&nbsp;</div>\\n<h1><span style=\\\"color: #339966;\\\"><em><strong><span lang=\\\"EN-US\\\">2.&nbsp;</span>台灣之星599 U25、799與599單門號:</strong></em></span></h1>\\n<div><span style=\\\"color: #000000;\\\">七月份最便宜的4G吃到飽資費仍是由台灣之星蟬連。目前台灣之星主打的低價4G吃到飽資費主要有<a style=\\\"color: #000000;\\\" href=\\\"http://www.tstartel.com/CWS/discount/04012015_student/index.htm\\\" target=\\\"_blank\\\">4G 599 U25</a>、4G 799及4G 599單門號。&nbsp;</span></div>\\n<div><span style=\\\"color: #000000;\\\">4G 599 U25限年滿12~25歲的年輕人才可以申辦，不但可享4G吃到飽而且有40分鐘網外分鐘數. 若不符資格，就必須申辦4G 799或是4G 599單門號方案。</span></div>\\n<div><span style=\\\"color: #000000;\\\">4G 599單門號方案，只要月租599元就可以享有4G 799的優惠內容(網外90分鐘)。因沒有手機補貼，只需綁約12個月，非常適合已經自備4G手機的用戶短期申辦。&nbsp;</span></div>\\n<p><span style=\\\"color: #339966;\\\"><em><strong><img id=\\\"C5TVlUo \\\" style=\\\"max-width: 100% !important; height: auto; display: block;\\\" src=\\\"http://i.imgur.com/C5TVlUo.jpg\\\" alt=\\\"\\\" /></strong></em></span></p>\\n<h3><span style=\\\"color: #ff6600;\\\">因台灣之星4G頻寬較小，若對台灣之星收訊或網速有疑慮的人，建議可以申請可以免費試用31天的方案(詳見<a style=\\\"color: #ff6600;\\\" href=\\\"http://www.tstartel.com/CWS/discount/05142015_0/index.htm\\\" target=\\\"_blank\\\">官網</a>)來體驗看看! &nbsp;&nbsp;</span></h3>\\n<p>&nbsp;</p>\\n<h1><span style=\\\"color: #33cccc;\\\"><span lang=\\\"EN-US\\\">3.&nbsp;&nbsp;</span>台灣大哥大 689/699 3G轉4G及4G&nbsp;998:</span></h1>\\n<p>台灣大哥大七月份除對外宣布4G 988方案將延長到七月底，在網路瘋傳的689及699 3G無痛轉4G吃到飽方案確定也延長到七月底。</p>\\n<p><span style=\\\"background-color: #ffff99;\\\">A.&nbsp;3G 689無痛轉4G方案:</span></p>\\n<p>新辦或是續約必須憑學生證才能申辦，若是攜碼則可以直接申辦。申辦成功後，即可本人親到直營門市申辦無痛升級優惠(受理該資費的店家通常會協助另外填寫3G升級4G申請書)，升級成功即可月繳689享有4G 799資費的優惠內容及4G上網吃到飽。</p>\\n<p><span style=\\\"background-color: #ffff99;\\\">B.&nbsp;3G 699(平板)無痛轉4G方案:</span></p>\\n<p>3G 699平板轉4G方案與3G 689方案申辦方式雷同，但不限學生資格。申辦完成後，可以到直營門市申辦無痛升級優惠，升級成功即可月繳699享有4G平板 899資費的優惠內容及4G上網吃到飽。&nbsp;</p>\\n<p>3G 699平板轉4G方案與學生3G 689轉4G最大的差異是699方案升級後不送通話費，打電話必須另外付費且費率比較高, 但終端裝置的補貼款較689高(24期攜碼約多千元; 36期攜碼約多4000元)。除非是拿來當平板使用或是平時無通話需求的人，版主基本上不建議申辦3G 699平板轉4G方案。</p>\\n<p><span style=\\\"background-color: #ffff99;\\\">C. 4G 998吃到飽:</span></p>\\n<p>4G 998相較3G 689轉4G的差異，除無學生資格限制及網外多10分鐘外，手機補貼款是三個4G吃到飽資費最高。因手機補貼款並未比689綁約期間省的月租多，若沒有打算購買萬元以上手機，版主比較推薦優先申辦學生3G 689轉4G。&nbsp;</p>\\n<h3><img id=\\\"g6lD5Gg \\\" style=\\\"max-width: 100% !important; height: auto; display: block;\\\" src=\\\"http://i.imgur.com/g6lD5Gg.jpg\\\" alt=\\\"\\\" /></h3>\\n<p><span style=\\\"color: #ff6600; background-color: #ffffff;\\\">另外，台灣大哥大針對平板電腦上網用戶，推出夏季限定「平板 4G 飆速送精選家電」方案，凡是新申辦/攜碼用戶，綁約 30 個月，月付 999 元，合約期間 4G 上網吃到飽不降速，加贈 32 吋三星 HD 液晶電視一台（市價 10,900 元），續約用戶可再享月租優惠，最低月付 899 元。</span></p>\\n<p><span style=\\\"color: #ff6600; background-color: #ffffff;\\\"><img id=\\\"RpeUmdN \\\" style=\\\"max-width: 100% !important; height: auto; display: block;\\\" src=\\\"http://i.imgur.com/RpeUmdN.jpg\\\" alt=\\\"\\\" /></span></p>\\n<p>&nbsp;</p>\\n<h1><span style=\\\"color: #800080;\\\"><span lang=\\\"EN-US\\\">4. &nbsp;</span>遠傳 學生及犀利攜碼689 3G轉4G及4G&nbsp;998:</span></h1>\\n<p>遠傳電信七月份跟進台灣大哥大，將4G 988吃到飽方案與六月新增3G學生698可以無痛升級698 4G吃到飽都延長到七月底(原本必須升級至4G 799才可吃到飽)。</p>\\n<p>遠傳電信學生3G 698無痛轉4G方案，若是新辦或是續約必須憑學生證才能申辦&nbsp;若是攜碼則可以直接申辦(犀利攜碼698)。申辦成功後，即可本人親到直營門市申辦無痛升級優惠(部分店家可以協助另外填寫3G升級4G申請書)，升級成功即可月繳698享有4G 799資費的優惠內容及4G上網吃到飽。</p>\\n<p>遠傳&nbsp;3G學生698可以無痛升級698 4G吃到飽方案的手機補貼款比台哥大3G 689學生案低，拉大與其4G 998資費的差異。不過，綁約期間(30個月)可以省下的月租費(9000元)，還是比4G 988多出來的佣金高一些。若沒有打算購買萬元以上手機，版主還是建議申辦3G學生698轉4G方案比較划算。若嫌麻煩或是有較多網外語音需求，建議優先申辦只有綁約24個月的4G 998。&nbsp;</p>\\n<p><img id=\\\"6iVwceH \\\" style=\\\"max-width: 100% !important; height: auto; display: block;\\\" src=\\\"http://i.imgur.com/6iVwceH.jpg\\\" alt=\\\"\\\" /></p>\\n<div>&nbsp;</div>\",\n" +
				"        \"bookmark\": [],\n" +
				"        \"file\": [\n" +
				"          \"6iVwceH.jpg\",\n" +
				"          \"C5TVlUo.jpg\",\n" +
				"          \"g6lD5Gg.jpg\",\n" +
				"          \"lgbsg6A.jpg\",\n" +
				"          \"RpeUmdN.jpg\"\n" +
				"        ],\n" +
				"        \"creator_user_name\": \"莊晏\",\n" +
				"        \"is_public\": true,\n" +
				"        \"modified\": \"false\",\n" +
				"        \"private_id\": \"\",\n" +
				"        \"id\": \"version1439367500497\",\n" +
				"        \"view_count\": 0\n" +
				"      }\n" +
				"    ],\n" +
				"    \"tags\": \"\"\n" +
				"  }\n" +
				"}";

		String response = "{\"id\":\"1009840175700426\",\"name\":\"范振原\",\"setting\":{\"wifi_sync\":true,\"mobile_network_sync\":true,\"version\":26,\"modified\":false,\"last_sync_time\":1455874940000},\"folder\":[{\"id\":\"allfolder\",\"name\":\"全部的懶人包\",\"pack\":[\"pack1439355907459\",\"pack1439367500493\",\"pack1439370245981\",\"pack1439372921598\",\"pack1439381800612\",\"pack1439385129482\",\"pack1439394796784\",\"pack1439451391246\",\"pack1439471856230\"]},{\"id\":\"allPackId\",\"name\":\"All\",\"pack\":[\"pack1439355907459\",\"pack1439370245981\",\"pack1439372921598\",\"pack1439381800612\",\"pack1439385129482\",\"pack1439394796784\",\"pack1450275282853\",\"pack1450347155192\"]},{\"id\":\"fjoeiwjowfe\",\"name\":\"我的最愛\",\"pack\":[]},{\"id\":\"shareFolder\",\"name\":\"與你分享懶人包\",\"pack\":[\"pack1439394796784\"]}],\"bookmark\":[]}";

		User user = objectMapper.readValue(response, User.class);

		when(userDAO.isExist("1009840175700426")).thenReturn(false);
		when(userDAO.getByUserId("1009840175700426")).thenReturn(user);

		Response result = resources.client().target("/user/sync").request().post(Entity.json(json));
		String entity = result.readEntity(String.class);


		verify(userDAO).isExist("1009840175700426");
		verify(userDAO).getByUserId("1009840175700426");

		JsonNode responseJsonNode = objectMapper.readTree(response);
		JsonNode resultJsonNode = objectMapper.readTree(entity);

		assertTrue(responseJsonNode.equals(resultJsonNode));

	}
}
