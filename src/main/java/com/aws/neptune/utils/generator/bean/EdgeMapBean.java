/*******************************************************************************
 *   Copyright 2018 Amazon.com, Inc. or its affiliates. All Rights Reserved.
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 *******************************************************************************/
package com.aws.neptune.utils.generator.bean;

import java.util.HashMap;
import java.util.Map;

public class EdgeMapBean {
    public Map<String, Object> maps = new HashMap<>();
    public EdgeMapBean(String labelName){
        this.maps.put("[EdgeLabel]", labelName);
//        Map<String, String> subMap = new HashMap<>();
//        Map<String, String> subMap2 = new HashMap<>();
//
//        subMap.put("Left", labelName + ".node_id");
//        this.maps.put("[edge_left]", subMap);
//        subMap2.put("Right", labelName + ".node_id");
//        this.maps.put("[edge_right]", subMap2);
    }
}
