/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.camel.dataformat.bindy.csv;

import org.apache.camel.EndpointInject;
import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.dataformat.bindy.model.simple.oneclassandtrimandclip.Customer;
import org.apache.camel.test.spring.junit5.CamelSpringTest;
import org.junit.jupiter.api.Test;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;

@ContextConfiguration
@CamelSpringTest
public class BindySimpleCsvMarshallTrimClipTest {

    private static final String URI_MOCK_RESULT = "mock:result";
    private static final String URI_DIRECT_START = "direct:start";

    private String expected;

    @Produce(URI_DIRECT_START)
    private ProducerTemplate template;

    @EndpointInject(URI_MOCK_RESULT)
    private MockEndpoint result;

    @Test
    @DirtiesContext
    public void testMarshallMessage() throws Exception {
        expected = "Mr,John,Doeeeeeeee,Cityyyyyyy\r\n";
        result.expectedBodiesReceived(expected);

        template.sendBody(generateModel());

        result.assertIsSatisfied();
    }

    public Object generateModel() {
        Customer customer = new Customer();
        customer.setSalutation("Mr");
        customer.setFirstName(" John ");
        customer.setLastName("Doeeeeeeeeeeeee");
        customer.setCity("Cityyyyyyy     ");
        return customer;
    }

    public static class ContextConfig extends RouteBuilder {
        @Override
        public void configure() {
            BindyCsvDataFormat camelDataFormat = new BindyCsvDataFormat(
                org.apache.camel.dataformat.bindy.model.simple.oneclassandtrimandclip.Customer.class);

            from(URI_DIRECT_START).marshal(camelDataFormat).to(URI_MOCK_RESULT);
        }
    }

}
