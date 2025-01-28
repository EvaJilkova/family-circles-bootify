package cz.familycircles.family_circles_bootify.model;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;

import cz.familycircles.family_circles_bootify.service.CategoryTypeService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Map;
import java.util.UUID;
import org.springframework.web.servlet.HandlerMapping;


/**
 * Validate that the name value isn't taken yet.
 */
@Target({ FIELD, METHOD, ANNOTATION_TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(
        validatedBy = CategoryTypeNameUnique.CategoryTypeNameUniqueValidator.class
)
public @interface CategoryTypeNameUnique {

    String message() default "{Exists.categoryType.name}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    class CategoryTypeNameUniqueValidator implements ConstraintValidator<CategoryTypeNameUnique, String> {

        private final CategoryTypeService categoryTypeService;
        private final HttpServletRequest request;

        public CategoryTypeNameUniqueValidator(final CategoryTypeService categoryTypeService,
                final HttpServletRequest request) {
            this.categoryTypeService = categoryTypeService;
            this.request = request;
        }

        @Override
        public boolean isValid(final String value, final ConstraintValidatorContext cvContext) {
            if (value == null) {
                // no value present
                return true;
            }
            @SuppressWarnings("unchecked") final Map<String, String> pathVariables =
                    ((Map<String, String>)request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE));
            final String currentId = pathVariables.get("categoryTypeId");
            if (currentId != null && value.equalsIgnoreCase(categoryTypeService.get(UUID.fromString(currentId)).getName())) {
                // value hasn't changed
                return true;
            }
            return !categoryTypeService.nameExists(value);
        }

    }

}
