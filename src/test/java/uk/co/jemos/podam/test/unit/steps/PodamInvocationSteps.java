package uk.co.jemos.podam.test.unit.steps;

import net.thucydides.core.annotations.Step;
import org.springframework.integration.core.MessagingTemplate;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.junit.Assert;

import uk.co.jemos.podam.api.ClassAttributeApprover;
import uk.co.jemos.podam.api.ClassInfo;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamUtils;
import uk.co.jemos.podam.common.PodamConstants;
import uk.co.jemos.podam.typeManufacturers.TypeManufacturerParamsWrapper;
import uk.co.jemos.podam.typeManufacturers.TypeManufacturerUtil;

import java.lang.reflect.Type;

/**
 * Created by tedonema on 27/05/2015.
 */
public class PodamInvocationSteps {

    @Step("When I invoke the factory manufacturing for {0}")
    public <T> T whenIInvokeTheFactoryForClass(Class<T> className, PodamFactory podamFactory) throws Exception {
        return podamFactory.manufacturePojo(className);
    }

    @Step("When I invoke the pojo's population directly for {0}")
    public <T> T whenIInvokeThePojoPopulationDirectly(T className, PodamFactory podamFactory) throws Exception {
        return podamFactory.populatePojo(className);
    }

    @Step("When I invoke the factory to manufacture {0} with the fullest constructor")
    public <T> T whenIInvokeTheFactoryForClassWithFullConstructor(Class<T> className, PodamFactory podamFactory)
    throws Exception {
        return podamFactory.manufacturePojoWithFullData(className);
    }

    @Step("When I invoke Podam Utils method to get class info for class {0} and approver {1}")
    public ClassInfo getClassInfo(Class<?> pojoClass, ClassAttributeApprover approver) {
        return PodamUtils.getClassInfo(pojoClass, approver);
    }

    @Step("When I invoke Podam for a Generic Pojo specifying the concrete types")
    public <T> T whenIInvokeTheFactoryForGenericTypeWithSpecificType(
            Class<T> pojoClass,
            PodamFactory podamFactory, Type... genericTypeArgs) {
        return podamFactory.manufacturePojo(pojoClass, genericTypeArgs);
    }

    @Step("When I send a Message to the channel")
    public Message<?> whenISendAMessageToTheChannel(TypeManufacturerParamsWrapper paramsWrapper, Class<?> type) {
        return whenISendAMessageToTheChannel(paramsWrapper, type.getName());
    }

    @Step("When I send a Message to the channel")
    public Message<?> whenISendAMessageToTheChannel(TypeManufacturerParamsWrapper paramsWrapper, String type) {
        MessageChannel inputChannel = PodamFactorySteps.givenAMessageChannelToManufactureValues();
        Assert.assertNotNull("Channel must exist", inputChannel);

        Message<?> message = TypeManufacturerUtil.createMessage(
                paramsWrapper, PodamConstants.HEADER_NAME,  type);
        Assert.assertNotNull("Channel must exist", inputChannel);

        MessagingTemplate template = new MessagingTemplate();
        return template.sendAndReceive(inputChannel, message);
    }
}
