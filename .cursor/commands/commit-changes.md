---
description: Commit changes following conventional commit format, grouped by feature
---

# Commit Changes by Feature

This command analyzes git changes, groups them by feature, and creates conventional commit messages in English.

## Process

1. **Analyze Changes**: Check git status to identify modified, added, and deleted files
2. **Infer Features**: Analyze file paths, package names, class names, and directory structure to identify features/domains. Features should be inferred from:
   - Directory structure and package organization
   - Class and file naming patterns
   - Domain concepts present in the codebase
   - Architectural layers (domain, application, infrastructure)
3. **Group by Feature**: Organize changes by inferred feature/scope. Each group should represent a cohesive business or technical domain.
4. **Determine Commit Type**: Based on the nature of changes:
   - `feat`: New features
   - `fix`: Bug fixes
   - `refactor`: Code refactoring
   - `docs`: Documentation changes
   - `style`: Code style changes (formatting, etc.)
   - `test`: Test-related changes
   - `chore`: Maintenance tasks
   - `perf`: Performance improvements
5. **Create Commit Messages**: Format as `<type>(<scope>): <description>`
6. **Commit Groups**: Create separate commits for each feature group

## Feature Detection Guidelines

Features should be inferred dynamically by analyzing:
- **File paths and directories**: Look for domain-specific directories, feature folders, or module structures
- **Package names**: Extract domain concepts from package hierarchies
- **Class names**: Identify domain entities, use cases, or services from class naming patterns
- **Architectural layers**: Distinguish between domain, application, infrastructure, and configuration concerns
- **Cross-cutting concerns**: Identify shared infrastructure, configuration, build, and documentation changes

The LLM should analyze the codebase structure to determine appropriate feature scopes rather than relying on a predefined list.

## Commit Message Format

```
<type>(<scope>): <description>

[optional body]

[optional footer]
```

### Examples

- `feat(auth): add user authentication with JWT tokens`
- `fix(payment): resolve transaction validation issue`
- `refactor(inventory): extract stock management logic to domain service`
- `docs(config): update database configuration documentation`
- `chore(build): update Spring Boot version to 3.5.6`

## Implementation Steps

1. Run `git status --porcelain` to get list of changed files
2. Analyze the codebase structure to understand feature organization:
   - Examine directory structure and package hierarchies
   - Review existing code organization patterns
   - Identify domain concepts and architectural boundaries
3. For each changed file:
   - Infer feature/scope by analyzing file path, package name, and class name
   - Determine change type (added, modified, deleted)
   - Infer commit type from file changes and context
4. Group files by inferred feature/scope, ensuring logical cohesion
5. For each group:
   - Generate descriptive commit message in English using inferred scope
   - Stage files for that group
   - Create commit with conventional format
6. Display summary of commits created

## Notes

- All commit messages must be in English
- Use imperative mood for commit messages (e.g., "add feature" not "added feature")
- Keep descriptions concise but descriptive
- Features should be inferred from the codebase structure, not from a predefined list
- Group related changes together by inferred feature/domain
- If a file touches multiple features, assign to the most specific feature based on the primary purpose
- Configuration, infrastructure, and build changes can be grouped separately if they affect multiple features
- Adapt to the project's architecture and naming conventions when inferring features
