package kitchenpos.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("OrderLineItems 클래스 테스트")
class OrderLineItemsTest {

    private final Order order = new Order(1L);
    private final List<OrderLineItem> orderLineItems = Arrays.asList(new OrderLineItem( 1L, 1L));

    @DisplayName("1개의 OrderLineItem로 OrderLineItems를 생성한다.")
    @Test
    void create() {
        OrderLineItems orderLineItems = new OrderLineItems();

        orderLineItems.addAll(this.order, this.orderLineItems);

        assertAll(
                () -> assertThat(orderLineItems.get()).hasSize(1),
                () -> assertThat(orderLineItems.get().get(0).getOrder()).isEqualTo(order)
        );
    }
}
