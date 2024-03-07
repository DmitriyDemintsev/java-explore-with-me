package ru.practicum.repository.query;

import com.querydsl.core.types.Expression;
import com.querydsl.core.types.dsl.*;
import lombok.AllArgsConstructor;
import org.hibernate.query.criteria.internal.ValueHandlerFactory;
import ru.practicum.model.Category;
import ru.practicum.model.Event;

import java.time.LocalDateTime;
import java.util.Collection;

@AllArgsConstructor
public class EventPredicate {

    private SearchCriteria criteria;

    public BooleanExpression getPredicate() {
        PathBuilder<Event> entityPath = new PathBuilder<>(Event.class, "event");

        if (criteria.getValue() instanceof Collection<?>) {
            SimplePath<Category> path = entityPath.getSimple(criteria.getKey(), Category.class);
            Collection<Category> value = (Collection<Category>) criteria.getValue();
            switch (criteria.getOperation()) {
                case "in":
                    return path.in(value);
                case "notIn":
                    return path.notIn(value);
            }
        } else if (ValueHandlerFactory.isNumeric(criteria.getValue())) {
            NumberPath<Integer> path = entityPath.getNumber(criteria.getKey(), Integer.class);
            int value = Integer.parseInt(criteria.getValue().toString());
            switch (criteria.getOperation()) {
                case "=":
                    return path.eq(value);
                case ">":
                    return path.goe(value);
                case "<":
                    return path.loe(value);
            }
        } else if (ValueHandlerFactory.isBoolean(criteria.getValue())) {
            BooleanPath path = entityPath.getBoolean(criteria.getKey());
            boolean value = Boolean.parseBoolean(criteria.getValue().toString());
            switch (criteria.getOperation()) {
                case "=":
                    return path.eq(value);
                case "!=":
                    return path.ne(value);
            }
        } else if (criteria.getValue() instanceof String) {
            StringPath path = entityPath.getString(criteria.getKey());
            switch (criteria.getOperation()) {
                case "=":
                    return path.containsIgnoreCase(criteria.getValue().toString());
                case "~":
                    return path.like('%' + criteria.getValue().toString().trim() + '%');
            }
        } else if (criteria.getValue() instanceof LocalDateTime) {
            DateTimePath<LocalDateTime> path = entityPath.getDateTime(criteria.getKey(), LocalDateTime.class);
            LocalDateTime value = (LocalDateTime) criteria.getValue();
            switch (criteria.getOperation()) {
                case "<":
                    return path.lt(value);
                case ">":
                    return path.gt(value);
                case "=":
                    return path.eq(value);
            }
        } else if (criteria.getValue() instanceof SimplePath) {
            NumberPath<Integer> path = entityPath.getNumber(criteria.getKey(), Integer.class);
            Expression<Integer> value = (Expression<Integer>) criteria.getValue();
            switch (criteria.getOperation()) {
                case "<":
                    return path.lt(value);
                case ">":
                    return path.gt(value);
                case "=":
                    return path.eq(value);
            }
        } else {
            SimplePath<Object> path = entityPath.getSimple(criteria.getKey(), Object.class);
            Object value = criteria.getValue();
            switch (criteria.getOperation()) {
                case "=":
                    return path.eq(value);
                case "!=":
                    return path.ne(value);
            }
        }
        return null;
    }
}
