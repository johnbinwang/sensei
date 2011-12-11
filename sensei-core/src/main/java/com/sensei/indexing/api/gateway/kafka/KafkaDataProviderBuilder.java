package com.sensei.indexing.api.gateway.kafka;

import java.util.Comparator;

import org.apache.commons.configuration.Configuration;
import org.json.JSONObject;

import proj.zoie.impl.indexing.StreamDataProvider;
import proj.zoie.impl.indexing.ZoieConfig;

import com.sensei.dataprovider.kafka.KafkaJsonStreamDataProvider;
import com.sensei.indexing.api.DataSourceFilter;
import com.sensei.indexing.api.gateway.SenseiGateway;

public class KafkaDataProviderBuilder extends SenseiGateway<byte[]>{

	public static final String name = "kafka";
	private final Comparator<String> _versionComparator;

	public KafkaDataProviderBuilder(Configuration conf){
	  super(conf);
	  _versionComparator = ZoieConfig.DEFAULT_VERSION_COMPARATOR;
	}

	@Override
	public StreamDataProvider<JSONObject> buildDataProvider(DataSourceFilter<byte[]> dataFilter,
			String oldSinceKey) throws Exception{

		String host = _conf.getString("host");
		int port = _conf.getInt("port");
		String topic = _conf.getString("topic");
		int timeout = _conf.getInt("timeout",10000);
		int batchsize = _conf.getInt("batchsize");
		long offset = oldSinceKey == null ? 0L : Long.parseLong(oldSinceKey);
		KafkaJsonStreamDataProvider provider = new KafkaJsonStreamDataProvider(_versionComparator, host,port,timeout,batchsize,topic,offset);
		if (dataFilter!=null){
		  provider.setFilter(dataFilter);
		}
		return provider;
	}

  @Override
  public Comparator<String> getVersionComparator() {
    return _versionComparator;
  }


}