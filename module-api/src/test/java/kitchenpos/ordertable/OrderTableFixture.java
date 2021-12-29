package kitchenpos.ordertable;

import kitchenpos.ordertable.dto.OrderTableRequest;

public class OrderTableFixture {
	public static OrderTableRequest 빈_주문_테이블_요청() {
		return new OrderTableRequest(4, true);
	}

	public static OrderTableRequest 비어있지않은_주문_테이블_요청() {
		return new OrderTableRequest(4, false);
	}

	public static OrderTableRequest 비우기_요청() {
		return new OrderTableRequest(true);
	}

	public static OrderTableRequest 채우기_요청() {
		return new OrderTableRequest(false);
	}

	public static OrderTableRequest 손님_수_변경_요청(int numberOfGuests) {
		return new OrderTableRequest(numberOfGuests);
	}
}