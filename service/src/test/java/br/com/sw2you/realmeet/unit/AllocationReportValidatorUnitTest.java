package br.com.sw2you.realmeet.unit;

import static br.com.sw2you.realmeet.validator.ValidatorConstants.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import br.com.sw2you.realmeet.core.BaseUnitTest;
import br.com.sw2you.realmeet.exception.InvalidRequestExcepiton;
import br.com.sw2you.realmeet.report.model.AllocationReportData;
import br.com.sw2you.realmeet.report.validator.AllocationReportValidator;
import br.com.sw2you.realmeet.validator.ValidationError;
import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AllocationReportValidatorUnitTest extends BaseUnitTest {
    private static final int MAX_MONTHS_INTERVAL = 6;
    private static final LocalDate PARAM_DATE_FROM = LocalDate.of(2030, 1, 1);
    private static final LocalDate PARAM_DATE_TO = LocalDate.of(2030, 4, 30);
    private static final String PARAM_EMAIL = "a@a.com";
    private AllocationReportValidator victim;

    private AllocationReportData.Builder reportDataBuilder;

    @BeforeEach
    void setupEach() {
        victim = new AllocationReportValidator(MAX_MONTHS_INTERVAL);
        reportDataBuilder =
            AllocationReportData.newBuilder().dateFrom(PARAM_DATE_FROM).dateTo(PARAM_DATE_TO).email(PARAM_EMAIL);
    }

    @Test
    void testValidateWhenReportDataIsValid() {
        victim.validate(reportDataBuilder.build());
    }

    @Test
    void testValidateWhenEmailIsMissing() {
        var exception = assertThrows(
            InvalidRequestExcepiton.class,
            () -> victim.validate(reportDataBuilder.email(null).build())
        );
        assertEquals(1, exception.getValidationErrors().getNumberOfErrors());
        assertEquals(new ValidationError(EMAIL, EMAIL + MISSING), exception.getValidationErrors().getError(0));
    }

    @Test
    void testValidateWhenDateFromIsMissing() {
        var exception = assertThrows(
            InvalidRequestExcepiton.class,
            () -> victim.validate(reportDataBuilder.dateFrom(null).build())
        );
        assertEquals(1, exception.getValidationErrors().getNumberOfErrors());
        assertEquals(new ValidationError(DATE_FROM, DATE_FROM + MISSING), exception.getValidationErrors().getError(0));
    }

    @Test
    void testValidateWhenDateToIsMissing() {
        var exception = assertThrows(
            InvalidRequestExcepiton.class,
            () -> victim.validate(reportDataBuilder.dateTo(null).build())
        );
        assertEquals(1, exception.getValidationErrors().getNumberOfErrors());
        assertEquals(new ValidationError(DATE_TO, DATE_TO + MISSING), exception.getValidationErrors().getError(0));
    }

    @Test
    void testValidateWhenDatesAreInverted() {
        var exception = assertThrows(
            InvalidRequestExcepiton.class,
            () -> victim.validate(reportDataBuilder.dateFrom(PARAM_DATE_TO).dateTo(PARAM_DATE_FROM).build())
        );
        assertEquals(1, exception.getValidationErrors().getNumberOfErrors());
        assertEquals(
            new ValidationError(DATE_FROM, DATE_FROM + INCONSISTENT),
            exception.getValidationErrors().getError(0)
        );
    }

    @Test
    void testValidateWhenDatesExceedsInterval() {
        var exception = assertThrows(
            InvalidRequestExcepiton.class,
            () -> victim.validate(reportDataBuilder.dateTo(PARAM_DATE_TO.plusMonths(MAX_MONTHS_INTERVAL + 1)).build())
        );
        assertEquals(1, exception.getValidationErrors().getNumberOfErrors());
        assertEquals(
            new ValidationError(DATE_TO, DATE_TO + EXCEEDS_INTERVAL),
            exception.getValidationErrors().getError(0)
        );
    }
}
