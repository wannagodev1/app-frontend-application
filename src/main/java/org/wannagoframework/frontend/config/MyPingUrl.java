/*
 * This file is part of the WannaGo distribution (https://github.com/wannago).
 * Copyright (c) [2019] - [2020].
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, version 3.
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package org.wannagoframework.frontend.config;

import com.netflix.appinfo.InstanceInfo;
import com.netflix.loadbalancer.IPing;
import com.netflix.loadbalancer.Server;
import com.netflix.niws.loadbalancer.DiscoveryEnabledServer;
import java.io.IOException;
import java.util.Map;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.json.JsonParser;
import org.springframework.boot.json.JsonParserFactory;


/**
 * Ping implementation if you want to do a "health check" kind of Ping. This will be a "real" ping.
 * As in a real http/s call is made to this url e.g. http://ec2-75-101-231-85.compute-1.amazonaws.com:7101/cs/hostRunning
 *
 * Some services/clients choose PingDiscovery - which is quick but is not a real ping. i.e It just
 * asks discovery (eureka) in-memory cache if the server is present in its Roster PingUrl on the
 * other hand, makes an actual call. This is more expensive - but its the "standard" way most VIPs
 * and other services perform HealthChecks.
 *
 * Choose your Ping based on your needs.
 *
 * @author stonse
 */
public class MyPingUrl implements IPing {

  private static final Logger LOGGER = LoggerFactory.getLogger(MyPingUrl.class);

  public MyPingUrl() {
  }

  public boolean isAlive(Server server) {
    boolean isAlive = false;
    if (server != null && server instanceof DiscoveryEnabledServer) {
      DiscoveryEnabledServer dServer = (DiscoveryEnabledServer) server;
      InstanceInfo instanceInfo = dServer.getInstanceInfo();
      String appName = instanceInfo.getAppName();
      String instanceStatus = instanceInfo.getStatus().toString();
      String urlStr = instanceInfo.getHealthCheckUrl();

      HttpClient httpClient = new DefaultHttpClient();
      HttpUriRequest getRequest = new HttpGet(urlStr);
      String content = null;
      try {
        HttpResponse response = httpClient.execute(getRequest);
        content = EntityUtils.toString(response.getEntity());
        LOGGER.trace("content:" + content);

        if (response.getStatusLine().getStatusCode() == 200) {
          JsonParser springParser = JsonParserFactory.getJsonParser();
          Map<String, Object> map = springParser.parseMap(content);
          isAlive = map.get("status").equals("UP");
          LOGGER.debug(appName + " server OK");
        } else {
          LOGGER.debug(appName + " server KO");
        }
      } catch (IOException e) {
        LOGGER
            .error("IO Exception with server '" + appName + "', instance status is '"
                + instanceStatus + "', url = '" + urlStr + "' : " + e.getLocalizedMessage());

      } catch (Exception e) {
        LOGGER.error(
            "Unknown Exception with server url " + urlStr + " : " + e.getLocalizedMessage(), e);
      } finally {
        // Release the connection.
        getRequest.abort();
      }
    }
    return isAlive;
  }
}
