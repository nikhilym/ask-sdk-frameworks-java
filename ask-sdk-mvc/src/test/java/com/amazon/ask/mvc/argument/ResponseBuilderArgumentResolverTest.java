/*
    Copyright 2018 Amazon.com, Inc. or its affiliates. All Rights Reserved.

    Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file
    except in compliance with the License. A copy of the License is located at

        http://aws.amazon.com/apache2.0/

    or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for
    the specific language governing permissions and limitations under the License.
 */

package com.amazon.ask.mvc.argument;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.model.RequestEnvelope;
import com.amazon.ask.mvc.SkillContext;
import com.amazon.ask.mvc.Utils;
import com.amazon.ask.mvc.plugin.ArgumentResolver;
import com.amazon.ask.mvc.mapper.ArgumentResolverContext;
import com.amazon.ask.mvc.mapper.MethodParameter;
import com.amazon.ask.response.ResponseBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ResponseBuilderArgumentResolverTest {
  private ArgumentResolver resolver = new ResponseBuilderArgumentResolver();

  @Mock
  SkillContext mockSkillContext;

  @Test
  public void testSupportAndResolve() throws NoSuchMethodException {
    MethodParameter methodParameter = new MethodParameter(
        this.getClass().getMethod("testSupportAndResolve"),
        0,
        ResponseBuilder.class,
        MethodParameter.EMPTY_ANNOTATIONS
    );

    RequestEnvelope envelope = Utils.buildSimpleEnvelope("intent");
    HandlerInput handlerInput =  HandlerInput.builder().withRequestEnvelope(envelope).build();
    ArgumentResolverContext input = new ArgumentResolverContext(mockSkillContext, methodParameter, handlerInput);

    assertTrue(resolver.resolve(input).get() instanceof ResponseBuilder);
  }

  @Test
  public void testResolvesNewInstance() throws NoSuchMethodException {
    MethodParameter methodParameter = new MethodParameter(
        this.getClass().getMethod("testSupportAndResolve"),
        0,
        ResponseBuilder.class,
        MethodParameter.EMPTY_ANNOTATIONS
    );

    RequestEnvelope envelope = Utils.buildSimpleEnvelope("intent");
    HandlerInput handlerInput =  HandlerInput.builder().withRequestEnvelope(envelope).build();
    ArgumentResolverContext input = new ArgumentResolverContext(mockSkillContext, methodParameter, handlerInput);

    Object resolved = resolver.resolve(input);
    Object secondResolved = resolver.resolve(input);

    assertNotSame(handlerInput.getResponseBuilder(), resolved);
    assertNotSame(secondResolved, resolved);
  }

  @Test
  public void testDoesntSupport() throws NoSuchMethodException {
    MethodParameter methodParameter = new MethodParameter(
        this.getClass().getMethod("testSupportAndResolve"),
        0,
        Object.class, //<---- wrong class
        MethodParameter.EMPTY_ANNOTATIONS
    );

    RequestEnvelope envelope = Utils.buildSimpleEnvelope("intent");
    ArgumentResolverContext input = new ArgumentResolverContext(mockSkillContext, methodParameter, HandlerInput.builder().withRequestEnvelope(envelope).build());

    assertFalse(resolver.resolve(input).isPresent());
  }
}
