package cz.familycircles.family_circles_bootify.model;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;

import cz.familycircles.family_circles_bootify.service.ItemService;
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
 * Validate that the itemName value isn't taken yet.
 */
@Target({ FIELD, METHOD, ANNOTATION_TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(
        validatedBy = ItemItemNameUnique.ItemItemNameUniqueValidator.class
)
public @interface ItemItemNameUnique {

    String message() default "{Exists.item.itemName}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    class ItemItemNameUniqueValidator implements ConstraintValidator<ItemItemNameUnique, String> {

        private final ItemService itemService;
        private final HttpServletRequest request;

        public ItemItemNameUniqueValidator(final ItemService itemService,
                final HttpServletRequest request) {
            this.itemService = itemService;
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
            final String currentId = pathVariables.get("itemId");
            if (currentId != null && value.equalsIgnoreCase(itemService.get(UUID.fromString(currentId)).getItemName())) {
                // value hasn't changed
                return true;
            }
            return !itemService.itemNameExists(value);
        }

    }

}
