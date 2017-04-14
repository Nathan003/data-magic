import com.dodoca.datamagic.es.bean.bean.EsClusterBean;
import com.dodoca.datamagic.es.bean.conn.ConnectionEsTool;
import org.elasticsearch.action.admin.cluster.health.ClusterHealthResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.cluster.health.ClusterHealthStatus;
import org.elasticsearch.cluster.health.ClusterIndexHealth;
import org.junit.Test;

/**
 * Created by admin on 2017/4/14.
 */
public class test {


    @Test
    public void test(){
        //ConnectionEsTool.insertIntoEsBySingle("",null,null,null);
        TransportClient transportClient = EsClusterBean.getInstance().getTransportClient();
        ClusterHealthResponse healths  = transportClient.admin().cluster().prepareHealth().get();
        String clusterName = healths.getClusterName();
        int numberOfDataNodes = healths.getNumberOfDataNodes();
        int numberOfNodes = healths.getNumberOfDataNodes();
        System.err.println("clusterName:" + clusterName+" numberOfDataNodes:"+numberOfDataNodes+" numberOfNodes:"+numberOfNodes);
        for (ClusterIndexHealth health : healths.getIndices().values()){
            String index = health.getIndex();
            int numberOfShards = health.getNumberOfShards();
            int numberOfReplicas = health.getNumberOfReplicas();
            ClusterHealthStatus status = health.getStatus();

            System.err.println("index:"+index+" numberOfShards:"+numberOfShards+" numberOfReplicas:"+numberOfReplicas
            +" status:"+status.toString());
        }
    }
}
