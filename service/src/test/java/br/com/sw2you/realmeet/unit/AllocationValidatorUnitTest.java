package br.com.sw2you.realmeet.unit;

import static br.com.sw2you.realmeet.util.DateUtils.now;
import static br.com.sw2you.realmeet.utils.TestDataCreator.newCreateAllocationDTO;
import static br.com.sw2you.realmeet.validator.ValidatorConstants.*;
import static org.apache.commons.lang3.StringUtils.rightPad;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import br.com.sw2you.realmeet.api.model.CreateAllocationDTO;
import br.com.sw2you.realmeet.core.BaseUnitTest;
import br.com.sw2you.realmeet.domain.repository.AllocationRepository;
import br.com.sw2you.realmeet.exception.InvalidRequestExcepiton;
import br.com.sw2you.realmeet.validator.AllocationValidator;
import br.com.sw2you.realmeet.validator.ValidationError;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

class AllocationValidatorUnitTest extends BaseUnitTest {
    private AllocationValidator victim;

    @Mock
    private AllocationRepository allocationRepository;

    @BeforeEach
    void setupEach() {
        victim = new AllocationValidator(allocationRepository);
    }

    @Test
    void testValidateWhenAllocationsIsValid() {
        victim.validate(newCreateAllocationDTO());
    }

    @Test
    void testValidateWhenSubjectIsMissing() {
        var exception = assertThrows(
            InvalidRequestExcepiton.class,
            () -> victim.validate(newCreateAllocationDTO().subject(null))
        );
        assertEquals(1, exception.getValidationErrors().getNumberOfErrors());
        assertEquals(
            new ValidationError(ALLOCATION_SUBJECT, ALLOCATION_SUBJECT + MISSING),
            exception.getValidationErrors().getError(0)
        );
    }

    @Test
    void testValidateWhenSubjectExceedsLength() {
        var exception = assertThrows(
            InvalidRequestExcepiton.class,
            () ->
                victim.validate(newCreateAllocationDTO().subject(rightPad("X", ALLOCATION_SUBJECT_MAX_LENGTH + 1, 'X')))
        );
        assertEquals(1, exception.getValidationErrors().getNumberOfErrors());
        assertEquals(
            new ValidationError(ALLOCATION_SUBJECT, ALLOCATION_SUBJECT + EXCEEDS_MAX_LENGTH),
            exception.getValidationErrors().getError(0)
        );
    }

    @Test
    void testValidateWhenEmployeeNameIsMissing() {
        var exception = assertThrows(
            InvalidRequestExcepiton.class,
            () -> victim.validate((CreateAllocationDTO) newCreateAllocationDTO().employeeName(null))
        );
        assertEquals(1, exception.getValidationErrors().getNumberOfErrors());
        assertEquals(
            new ValidationError(ALLOCATION_EMPLOYEE_NAME, ALLOCATION_EMPLOYEE_NAME + MISSING),
            exception.getValidationErrors().getError(0)
        );
    }

    @Test
    void testValidateWhenNametExceedsLength() {
        var exception = assertThrows(
            InvalidRequestExcepiton.class,
            () ->
                victim.validate(
                    newCreateAllocationDTO().employeeName(rightPad("X", ALLOCATION_EMPLOYEE_NAME_MAX_LENGTH + 1, 'X'))
                )
        );
        assertEquals(1, exception.getValidationErrors().getNumberOfErrors());
        assertEquals(
            new ValidationError(ALLOCATION_EMPLOYEE_NAME, ALLOCATION_EMPLOYEE_NAME + EXCEEDS_MAX_LENGTH),
            exception.getValidationErrors().getError(0)
        );
    }

    @Test
    void testValidateWhenEmployeeEmailIsMissing() {
        var exception = assertThrows(
            InvalidRequestExcepiton.class,
            () -> victim.validate((CreateAllocationDTO) newCreateAllocationDTO().employeeEmail(null))
        );
        assertEquals(1, exception.getValidationErrors().getNumberOfErrors());
        assertEquals(
            new ValidationError(ALLOCATION_EMPLOYEE_EMAIL, ALLOCATION_EMPLOYEE_EMAIL + MISSING),
            exception.getValidationErrors().getError(0)
        );
    }

    @Test
    void testValidateWhenEmailtExceedsLength() {
        var exception = assertThrows(
            InvalidRequestExcepiton.class,
            () ->
                victim.validate(
                    newCreateAllocationDTO().employeeEmail(rightPad("X", ALLOCATION_EMPLOYEE_EMAIL_MAX_LENGTH + 1, 'X'))
                )
        );
        assertEquals(1, exception.getValidationErrors().getNumberOfErrors());
        assertEquals(
            new ValidationError(ALLOCATION_EMPLOYEE_EMAIL, ALLOCATION_EMPLOYEE_EMAIL + EXCEEDS_MAX_LENGTH),
            exception.getValidationErrors().getError(0)
        );
    }

    @Test
    void testValidateWhenStartAttIsMissing() {
        var exception = assertThrows(
            InvalidRequestExcepiton.class,
            () -> victim.validate(newCreateAllocationDTO().startAt(null))
        );
        assertEquals(1, exception.getValidationErrors().getNumberOfErrors());
        assertEquals(
            new ValidationError(ALLOCATION_START_AT, ALLOCATION_START_AT + MISSING),
            exception.getValidationErrors().getError(0)
        );
    }

    @Test
    void testValidateWhenEndtAttIsMissing() {
        var exception = assertThrows(
            InvalidRequestExcepiton.class,
            () -> victim.validate(newCreateAllocationDTO().endAt(null))
        );
        assertEquals(1, exception.getValidationErrors().getNumberOfErrors());
        assertEquals(
            new ValidationError(ALLOCATION_END_AT, ALLOCATION_END_AT + MISSING),
            exception.getValidationErrors().getError(0)
        );
    }

    @Test
    void testValidateWhenDateOrderIsInvalid() {
        var exception = assertThrows(
            InvalidRequestExcepiton.class,
            () ->
                victim.validate(
                    newCreateAllocationDTO().startAt(now().plusDays(1)).endAt(now().plusDays(1).minusMinutes(30))
                )
        );
        assertEquals(1, exception.getValidationErrors().getNumberOfErrors());
        assertEquals(
            new ValidationError(ALLOCATION_START_AT, ALLOCATION_START_AT + INCONSISTENT),
            exception.getValidationErrors().getError(0)
        );
    }

    @Test
    void testValidateWhenDateIsInThePast() {
        var exception = assertThrows(
            InvalidRequestExcepiton.class,
            () -> victim.validate(newCreateAllocationDTO().startAt(now().minusMinutes(30)).endAt(now().plusMinutes(30)))
        );
        assertEquals(1, exception.getValidationErrors().getNumberOfErrors());
        assertEquals(
            new ValidationError(ALLOCATION_START_AT, ALLOCATION_START_AT + IN_THE_PAST),
            exception.getValidationErrors().getError(0)
        );
    }

    @Test
    void testValidateWhenDateIntervalExceedsMaxDuration() {
        var exception = assertThrows(
            InvalidRequestExcepiton.class,
            () ->
                victim.validate(
                    newCreateAllocationDTO()
                        .startAt(now().plusDays(1))
                        .endAt(now().plusDays(1).plusSeconds(ALLOCATION_MAX_DURATION_SECONDS + 1))
                )
        );
        assertEquals(1, exception.getValidationErrors().getNumberOfErrors());
        assertEquals(
            new ValidationError(ALLOCATION_END_AT, ALLOCATION_END_AT + EXCEEDS_DURATION),
            exception.getValidationErrors().getError(0)
        );
    }
}
