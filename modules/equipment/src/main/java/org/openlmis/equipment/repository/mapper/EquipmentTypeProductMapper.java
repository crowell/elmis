/*
 * This program was produced for the U.S. Agency for International Development. It was prepared by the USAID | DELIVER PROJECT, Task Order 4. It is part of a project which utilizes code originally licensed under the terms of the Mozilla Public License (MPL) v2 and therefore is licensed under MPL v2 or later.
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the Mozilla Public License as published by the Mozilla Foundation, either version 2 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the Mozilla Public License for more details.
 *
 * You should have received a copy of the Mozilla Public License along with this program. If not, see http://www.mozilla.org/MPL/
 */

package org.openlmis.equipment.repository.mapper;

import org.apache.ibatis.annotations.*;
import org.openlmis.core.domain.Product;
import org.openlmis.equipment.domain.EquipmentTypeProduct;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EquipmentTypeProductMapper {

  @Select("select pep.*, p.fullName productName, p.primaryName " +
      "      from equipment_type_products pep " +
      "      join products p on p.id = pep.productId " +
      "      where programEquipmentTypeId = #{programEquipmentTypeId} order by productName")
  @Results(value = {
      @Result(property = "product.fullName",column = "productName"),
      @Result(property="product.primaryName",column = "primaryName")
  })
  List<EquipmentTypeProduct> getByProgramEquipmentId(@Param(value="programEquipmentTypeId") Long programEquipmentTypeId);

  @Insert("INSERT INTO equipment_type_products (programEquipmentTypeId, productId, createdBy, createdDate, modifiedBy, modifiedDate) " +
      "VALUES (#{programEquipmentType.id}, #{product.id}, #{createdBy}, #{createdDate}, #{modifiedBy}, #{modifiedDate}) ")
  @Options(useGeneratedKeys = true)
  void insert(EquipmentTypeProduct equipmentTypeProduct);

  @Update("UPDATE equipment_type_products " +
      "SET programEquipmentTypeId = #{programEquipmentType.id}, productId = #{product.id}, createdBy = #{createdBy}, createdDate = #{createdDate}, modifiedBy = #{modifiedBy}, modifiedDate = #{modifiedDate} " +
      "WHERE id = #{id}")
  void update(EquipmentTypeProduct equipmentTypeProduct);

  @Delete("DELETE FROM equipment_type_products WHERE id = #{programEquipmentProductId}")
  void remove(@Param(value = "programEquipmentProductId") Long programEquipmentProductId);

  @Delete("DELETE FROM equipment_type_products WHERE programEquipmentTypeId = #{programEquipmentTypeId}")
  void removeByEquipmentProducts(@Param(value = "programEquipmentTypeId") Long programEquipmentTypeId);

  @Select("SELECT p.* from products p " +
    "       join program_products pp on pp.productId = p.id " +
    "     where " +
    "           pp.programId = #{programId} " +
    "     order by pp.displayOrder")
  List<Product> getAvailableProductsToLink(@Param("programId") Long programId, @Param("equipmentTypeId") Long equipmentTypeId);
}
