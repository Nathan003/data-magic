import com.dodoca.datamagic.es.bean.bean.EsClusterBean;
//import com.dodoca.datamagic.es.bean.conn.ConnectionEsOrder;
//ssimport com.dodoca.datamagic.es.bean.conn.ConnectionEsTool;
import org.elasticsearch.action.admin.cluster.health.ClusterHealthResponse;
import org.elasticsearch.action.admin.cluster.node.info.NodeInfo;
import org.elasticsearch.action.admin.cluster.node.info.NodesInfoRequest;
import org.elasticsearch.action.admin.cluster.node.info.NodesInfoResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.cluster.health.ClusterHealthStatus;
import org.elasticsearch.cluster.health.ClusterIndexHealth;
import org.junit.Test;

import java.util.Map;

/**
 * Created by admin on 2017/4/14.
 */
public class test {


//    @Test
//    public void test() {
//        //ConnectionEsTool.insertIntoEsBySingle("",null,null,null);
//        TransportClient transportClient = EsClusterBean.getInstance().getTransportClient();
//        ClusterHealthResponse healths = transportClient.admin().cluster().prepareHealth().get();
//        String clusterName = healths.getClusterName();
//        int numberOfDataNodes = healths.getNumberOfDataNodes();
//        int numberOfNodes = healths.getNumberOfDataNodes();
//        System.err.println("clusterName:" + clusterName + " numberOfDataNodes:" + numberOfDataNodes + " numberOfNodes:" + numberOfNodes);
//        for (ClusterIndexHealth health : healths.getIndices().values()) {
//            String index = health.getIndex();
//            int numberOfShards = health.getNumberOfShards();
//            int numberOfReplicas = health.getNumberOfReplicas();
//            ClusterHealthStatus status = health.getStatus();
//
//            System.err.println("index:" + index + " numberOfShards:" + numberOfShards + " numberOfReplicas:" + numberOfReplicas
//                    + " status:" + status.toString());
//        }
//    }
//
//
//    @Test
//    public void test1() {
//        TransportClient transportClient = EsClusterBean.getInstance().getTransportClient();
//        try {
//            NodesInfoResponse response = transportClient.admin().cluster().nodesInfo(new NodesInfoRequest().timeout("30")).actionGet();
//            Map<String, NodeInfo> nodesMap = response.getNodesMap();
//
//            for (Map.Entry<String, NodeInfo> entry : nodesMap.entrySet()) {
//                System.err.println(entry.getKey() + ":" + entry.getValue().getServiceAttributes());
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            System.err.println("无法连接到Elasticsearch");
//        }
//    }
//
//    @Test
//    public void test2(){
//
//    }
}
