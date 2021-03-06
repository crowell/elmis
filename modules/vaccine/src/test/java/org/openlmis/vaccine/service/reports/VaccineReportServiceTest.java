package org.openlmis.vaccine.service.reports;

import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.openlmis.core.builder.ProcessingPeriodBuilder;
import org.openlmis.core.domain.ProcessingPeriod;
import org.openlmis.core.domain.ProgramProduct;
import org.openlmis.core.repository.ProcessingPeriodRepository;
import org.openlmis.core.service.ProgramProductService;
import org.openlmis.core.service.ProgramService;
import org.openlmis.db.categories.UnitTests;
import org.openlmis.vaccine.RequestStatus;
import org.openlmis.vaccine.builders.reports.VaccineReportBuilder;
import org.openlmis.vaccine.domain.VaccineDisease;
import org.openlmis.vaccine.domain.VaccineProductDose;
import org.openlmis.vaccine.domain.Vitamin;
import org.openlmis.vaccine.domain.VitaminSupplementationAgeGroup;
import org.openlmis.vaccine.domain.config.VaccineIvdTabVisibility;
import org.openlmis.vaccine.domain.reports.ColdChainLineItem;
import org.openlmis.vaccine.domain.reports.VaccineReport;
import org.openlmis.vaccine.dto.ReportStatusDTO;
import org.openlmis.vaccine.repository.VitaminRepository;
import org.openlmis.vaccine.repository.VitaminSupplementationAgeGroupRepository;
import org.openlmis.vaccine.repository.reports.VaccineReportColdChainRepository;
import org.openlmis.vaccine.repository.reports.VaccineReportRepository;
import org.openlmis.vaccine.service.DiseaseService;
import org.openlmis.vaccine.service.VaccineIvdTabVisibilityService;
import org.openlmis.vaccine.service.VaccineProductDoseService;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.natpryce.makeiteasy.MakeItEasy.a;
import static com.natpryce.makeiteasy.MakeItEasy.make;
import static java.util.Arrays.asList;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;


@Category(UnitTests.class)
@RunWith(MockitoJUnitRunner.class)
public class VaccineReportServiceTest {

  @Mock
  VaccineReportRepository repository;

  @Mock
  ProgramProductService programProductService;

  @Mock
  VaccineLineItemService lLineItemService;

  @Mock
  DiseaseService diseaseService;

  @Mock
  ProcessingPeriodRepository periodService;

  @Mock
  VaccineProductDoseService productDoseService;

  @Mock
  VaccineIvdTabVisibilityService settingService;

  @Mock
  VaccineReportColdChainRepository coldChainRepository;

  @Mock
  VitaminRepository vitaminRepository;

  @Mock
  VitaminSupplementationAgeGroupRepository ageGroupRepository;

  @Mock
  ProgramService programService;

  @InjectMocks
  VaccineReportService service;

  @Before
  public void setup() throws Exception{
    when(programProductService.getActiveByProgram(1L))
      .thenReturn(new ArrayList<ProgramProduct>());
    when(productDoseService.getForProgram(1L))
      .thenReturn(asList(new VaccineProductDose()));
    when(diseaseService.getAll())
      .thenReturn(new ArrayList<VaccineDisease>());
    when(coldChainRepository.getNewEquipmentLineItems(1L,1L))
      .thenReturn(new ArrayList<ColdChainLineItem>());
    when(vitaminRepository.getAll())
      .thenReturn(new ArrayList<Vitamin>());
    when(ageGroupRepository.getAll())
      .thenReturn(new ArrayList<VitaminSupplementationAgeGroup>());

  }


  @Test
  public void shouldReturnExistingRecordIfAlreadyInitialized() throws Exception{
    VaccineReport report = make(a(VaccineReportBuilder.defaultVaccineReport));
    when(repository.getByProgramPeriod(1L, 1L, 1L)).thenReturn(report);

    VaccineReport result = service.initialize(1L, 1L, 1L);
    verify(programProductService, never() ).getActiveByProgram(1L);
    assertThat(result, is(report));
  }

  @Test
  public void shouldInitializeWhenRecordIsNotFound() throws Exception {
    VaccineReport report = make(a(VaccineReportBuilder.defaultVaccineReport));

    when(repository.getByProgramPeriod(1L, 1L, 1L)).thenReturn(null);

    doNothing().when(repository).insert(report);

    VaccineReport result = service.initialize(1L, 1L, 1L);

    verify(repository).insert(Matchers.any(VaccineReport.class));
  }

  @Test
  public void shouldGetPeriodsFor() throws Exception {

  }

  @Test
  public void shouldSave() throws Exception {
    VaccineReport report = make(a(VaccineReportBuilder.defaultVaccineReport));
    doNothing().when(repository).update(report);
    service.save(report);
    verify(repository).update(report);
  }

  @Test
  public void shouldGetById() throws Exception {
    VaccineReport report = make(a(VaccineReportBuilder.defaultVaccineReport));
    when(repository.getByIdWithFullDetails(2L)).thenReturn(report);
    when(settingService.getVisibilityForProgram(report.getProgramId())).thenReturn(new ArrayList<VaccineIvdTabVisibility>());

    VaccineReport result = service.getById(2L);
    verify(repository).getByIdWithFullDetails(2L);
    assertThat(result.getStatus(), is(report.getStatus()));
  }

  @Test
  public void shouldSubmit() throws Exception {
    VaccineReport report = make(a(VaccineReportBuilder.defaultVaccineReport));
    doNothing().when(repository).update(report);
    service.submit(report);
    verify(repository).update(report);
    assertThat(report.getStatus(), is(RequestStatus.SUBMITTED.toString()));
  }

  @Test
  public void shouldGetReportedPeriodsForFacility() throws Exception{
    service.getReportedPeriodsFor(2L, 3L);
    verify(repository).getReportedPeriodsForFacility(2L, 3L);
  }

  @Test
  public void shouldGetPeriodsEmptyListOfPeriodsForFacility()throws  Exception {
    Date startDate = new Date(2015,2,2);
    Date endDate = new Date();

    when(repository.getLastReport(2L, 2L)).thenReturn(null);
    when(programService.getProgramStartDate(2L, 2L)).thenReturn(startDate);
    when(periodService.getAllPeriodsForDateRange(1L, startDate, endDate)).thenReturn(null);

    List<ReportStatusDTO> response = service.getPeriodsFor(2L, 2L, endDate);
    assertThat(response.size(), is(0));
  }

  @Test
  public void shouldGetPeriodsListOfPeriodsForFacility()throws  Exception {
    Date startDate = new Date(2015,2,2);
    Date endDate = new Date();
    when(repository.getLastReport(2L, 2L)).thenReturn(null);
    when(repository.getScheduleFor(2L, 2L)).thenReturn(1L);
    when(programService.getProgramStartDate(2L, 2L)).thenReturn(startDate);
    List<ProcessingPeriod> periods = asList(make(a(ProcessingPeriodBuilder.defaultProcessingPeriod)));
    when(periodService.getAllPeriodsForDateRange(1L, startDate, endDate)).thenReturn(periods);

    List<ReportStatusDTO> response = service.getPeriodsFor(2L, 2L, endDate);

    assertThat(response.size(), is(1));
  }
}