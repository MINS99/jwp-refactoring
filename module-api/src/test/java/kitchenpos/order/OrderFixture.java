package kitchenpos.order;

import java.util.Collections;

import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.dto.OrderLineItemDto;
import kitchenpos.order.dto.OrderRequest;

public class OrderFixture {
	public static OrderRequest 주문_요청(Long orderTableId, Long menuId, int quantity) {
		return new OrderRequest(orderTableId, Collections.singletonList(new OrderLineItemDto(menuId, quantity)));
	}

	public static OrderRequest 주문_항목_없는_주문_요청(Long orderTableId) {
		return new OrderRequest(orderTableId, Collections.emptyList());
	}

	public static OrderRequest 주문_상태_변경_요청(OrderStatus orderStatus) {
		return new OrderRequest(orderStatus);
	}
}