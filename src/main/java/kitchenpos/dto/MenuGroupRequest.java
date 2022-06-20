package kitchenpos.dto;

import kitchenpos.domain.MenuGroup;

public class MenuGroupRequest {
    private String name;

    public MenuGroupRequest(String name) {
        this.name = name;
    }

    protected MenuGroupRequest() {
    }

    public MenuGroup toMenuGroup() {
        return new MenuGroup(name);
    }

    public String getName() {
        return name;
    }
}
