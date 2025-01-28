package cz.familycircles.family_circles_bootify.model;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;

import cz.familycircles.family_circles_bootify.service.UserService;
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
 * Validate that the userName value isn't taken yet.
 */
@Target({ FIELD, METHOD, ANNOTATION_TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(
        validatedBy = UserUserNameUnique.UserUserNameUniqueValidator.class
)
public @interface UserUserNameUnique {

    String message() default "{Exists.user.userName}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    class UserUserNameUniqueValidator implements ConstraintValidator<UserUserNameUnique, String> {

        private final UserService userService;
        private final HttpServletRequest request;

        public UserUserNameUniqueValidator(final UserService userService,
                final HttpServletRequest request) {
            this.userService = userService;
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
            final String currentId = pathVariables.get("userId");
            if (currentId != null && value.equalsIgnoreCase(userService.get(UUID.fromString(currentId)).getUserName())) {
                // value hasn't changed
                return true;
            }
            return !userService.userNameExists(value);
        }

    }

}
