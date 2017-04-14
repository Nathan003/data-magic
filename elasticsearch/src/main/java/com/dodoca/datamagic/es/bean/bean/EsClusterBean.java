package com.dodoca.datamagic.es.bean.bean;

import com.dodoca.datamagic.es.bean.database.LoadConfigurationInfo ;
import org.apache.log4j.Logger;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Properties;

public class EsClusterBean {

	private static Logger logger =  Logger.getLogger(EsClusterBean.class);
	private String zkConnect;
	private String clusterName;
	private String topic;
	private String index;
	private String types;
	private int port;
	private String esNodes = null;
	private TransportClient transportClient = null;
	private static EsClusterBean es = new EsClusterBean();
	private Properties props = new Properties();
	public static EsClusterBean getInstance() {
		return es;
	}

	private EsClusterBean() {
		try {
			Properties prop = LoadConfigurationInfo.getInstance().getInterfaceProperties();
			zkConnect = prop.getProperty("zkConnect");
			clusterName = prop.getProperty("clusterName");
			topic = prop.getProperty("topic");
			index = prop.getProperty("index");
			types = prop.getProperty("types");
			port = Integer.parseInt(prop.getProperty("port"));
			esNodes = prop.getProperty("esNodes");
			init();
			initProperties();
			logger.info(toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public TransportClient getTransportClient(){
		return transportClient;
	}
	
	private void initProperties(){
		props.put("zookeeper.connect",  zkConnect);
		// group 代表一个消费组
		props.put("group.id", "order");
		// zk连接超时
		props.put("zookeeper.session.timeout.ms", "60000");
		props.put("zookeeper.sync.time.ms", "200");
		props.put("auto.commit.interval.ms", "1000");
		// 有smallest、largest、anything可选，分别表示给当前最小的offset、当前最大的offset、抛异常。默认largest
		props.put("auto.offset.reset", "largest");
	}
	
	public Properties getZkProperties(){
		return props;
	}

	public String getZkConnect() {
		return zkConnect;
	}

	public void setZkConnect(String zkConnect) {
		this.zkConnect = zkConnect;
	}

	public String getClusterName() {
		return clusterName;
	}

	public String getTopic() {
		return topic;
	}

	public String getIndex() {
		return index;
	}

	public String getTypes() {
		return types;
	}

	public int getPort() {
		return port;
	}

	public String getEsNodes() {
		return esNodes;
	}
	
	private void init(){
		//Settings settings = Settings.settingsBuilder().put("cluster.name", clusterName).put("client.transport.sniff", true).build();
		Settings settings = Settings.settingsBuilder().put("cluster.name", clusterName).build();
		if (esNodes ==null || esNodes.isEmpty())
			return;
		String[] arr = esNodes.split(",");
		transportClient = TransportClient.builder().settings(settings).build();
		for (int i= 0;i<arr.length;i++){
			try {
				transportClient.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(arr[i]), port));
			} catch (UnknownHostException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
}	
	
	@Override
	public String toString() {
		return "EsClusterBean [zkConnect=" + zkConnect + ", clusterName=" + clusterName + ", topic=" + topic
				+ ", index=" + index + ", types=" + types + ", port=" + port + ", esNodes=" + esNodes + ", props="
				+ props + "]";
	}

}
