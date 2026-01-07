package com.backend.flowershop.domain.model.common;

import java.util.Objects;

/**
 * Address：地址值对象（冻结文件）

 * 职责边界：
 * 1) 领域层表达“地址”的语义（不可变）
 * 2) 只做最小非空约束，避免过度严格导致扩展困难
 * 3) 更复杂的地址拆分/标准化（例如邮编、州、省）放到 application/normalize 与 validator

 * 字段说明（最小集合）：
 * - line1：详细地址第一行（必填）
 * - line2：第二行（可选）
 * - city：城市（可选）
 * - state：州/省（可选）
 * - postalCode：邮编（可选）
 * - country：国家（可选）
 */
public final class Address {

    private final String line1;
    private final String line2;
    private final String city;
    private final String state;
    private final String postalCode;
    private final String country;

    public Address(
            String line1,
            String line2,
            String city,
            String state,
            String postalCode,
            String country
    ) {
        if (line1 == null || line1.isBlank()) {
            throw new IllegalArgumentException("Address.line1 不能为空");
        }

        this.line1 = line1.trim();
        this.line2 = (line2 == null) ? null : line2.trim();
        this.city = (city == null) ? null : city.trim();
        this.state = (state == null) ? null : state.trim();
        this.postalCode = (postalCode == null) ? null : postalCode.trim();
        this.country = (country == null) ? null : country.trim();
    }

    public String line1() { return line1; }
    public String line2() { return line2; }
    public String city() { return city; }
    public String state() { return state; }
    public String postalCode() { return postalCode; }
    public String country() { return country; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Address)) return false;
        Address address = (Address) o;
        return Objects.equals(line1, address.line1)
                && Objects.equals(line2, address.line2)
                && Objects.equals(city, address.city)
                && Objects.equals(state, address.state)
                && Objects.equals(postalCode, address.postalCode)
                && Objects.equals(country, address.country);
    }

    @Override
    public int hashCode() {
        return Objects.hash(line1, line2, city, state, postalCode, country);
    }

    @Override
    public String toString() {
        return "Address{" +
                "line1='" + line1 + '\'' +
                ", line2='" + line2 + '\'' +
                ", city='" + city + '\'' +
                ", state='" + state + '\'' +
                ", postalCode='" + postalCode + '\'' +
                ", country='" + country + '\'' +
                '}';
    }
}
