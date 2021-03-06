/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.spark.status.api.v1

import javax.ws.rs.{GET, Produces}
import javax.ws.rs.core.MediaType

import org.apache.spark.sql.oap.ui.{FiberCacheManagerPage, FiberCacheManagerSummary}
import org.apache.spark.ui.SparkUI

@Produces(Array(MediaType.APPLICATION_JSON))
private[v1] class AllFiberCacheManagerListResource(ui: SparkUI) {

  @GET
  def executorList(): Seq[FiberCacheManagerSummary] = {
    val listener = ui.executorsListener
    listener.synchronized {
      // The follow codes should be protected by `listener` to make sure no executors will be
      // removed before we query their status. See SPARK-12784.
      (0 until listener.activeStorageStatusList.size).map { statusId =>
        FiberCacheManagerPage.getFiberCacheManagerInfo(listener, statusId)
      }
    }
  }
}
