/*
 * This program was produced for the U.S. Agency for International Development. It was prepared by the USAID | DELIVER PROJECT, Task Order 4. It is part of a project which utilizes code originally licensed under the terms of the Mozilla Public License (MPL) v2 and therefore is licensed under MPL v2 or later.
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the Mozilla Public License as published by the Mozilla Foundation, either version 2 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the Mozilla Public License for more details.
 *
 * You should have received a copy of the Mozilla Public License along with this program. If not, see http://www.mozilla.org/MPL/
 */

package org.openlmis.web.controller.vaccine;

import org.openlmis.vaccine.dto.VaccineServiceConfigDTO;
import org.openlmis.vaccine.service.VaccineProductDoseService;
import org.openlmis.web.controller.BaseController;
import org.openlmis.web.response.OpenLmisResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping(value = "/vaccine/product-dose/")
public class ProductDoseController extends BaseController {

  @Autowired
  private VaccineProductDoseService service;

  @RequestMapping(value = "get/{programId}")
  public ResponseEntity<OpenLmisResponse> getProgramProtocol(@PathVariable Long programId) {
    VaccineServiceConfigDTO dto = service.getProductDoseForProgram(programId);
    return OpenLmisResponse.response("protocol", dto);
  }

  @RequestMapping(value = "save", headers = ACCEPT_JSON, method = RequestMethod.PUT)
  public ResponseEntity<OpenLmisResponse> save(@RequestBody VaccineServiceConfigDTO config) {
    service.save(config.getProtocols());
    return OpenLmisResponse.response("protocol", config);
  }

}
