package cz.familycircles.family_circles_bootify.model;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;

import cz.familycircles.family_circles_bootify.service.ActionService;
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
        validatedBy = ActionNameUnique.ActionNameUniqueValidator.class
)
public @interface ActionNameUnique {

    String message() default "{Exists.action.name}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    class ActionNameUniqueValidator implements ConstraintValidator<ActionNameUnique, String> {

        private final ActionService actionService;
        private final HttpServletRequest request;

        public ActionNameUniqueValidator(final ActionService actionService,
                final HttpServletRequest request) {
            this.actionService = actionService;
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
            final String currentId = pathVariables.get("actionId");
            if (currentId != null && value.equalsIgnoreCase(actionService.get(UUID.fromString(currentId)).getName())) {
                // value hasn't changed
                return true;
            }
            return !actionService.nameExists(value);
        }

    }

}
