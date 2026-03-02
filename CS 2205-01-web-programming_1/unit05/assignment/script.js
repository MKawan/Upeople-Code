const taskInput = document.getElementById('taskInput');
const addBtn = document.getElementById('addBtn');
const taskList = document.getElementById('taskList');

// Add new task
addBtn.addEventListener('click', () => {
    const text = taskInput.value.trim();
    if (text === '') return;

    const li = document.createElement('li');
    li.innerHTML = `
        <input type="checkbox" class="complete-checkbox">
        <span class="task-text">${text}</span>
        <button class="edit-btn"><span class="material-symbols-outlined">edit</span></button>
        <button class="remove-btn"><span class="material-symbols-outlined">delete</span></button>
      `;
    taskList.appendChild(li);
    taskInput.value = '';
});

// Event delegation on the task list container
taskList.addEventListener('click', (e) => {
    const target = e.target;

    // Handle Remove button
    if (target.classList.contains('remove-btn')) {
        target.parentElement.remove();
        return;
    }

    // Handle Edit button
    if (target.classList.contains('edit-btn')) {
        const li = target.parentElement;
        const taskText = li.querySelector('.task-text');
        const currentText = taskText.textContent;

        // Use prompt as a simple dialog for editing (as per assignment note)
        const newText = prompt('Edit your task:', currentText);
        if (newText !== null && newText.trim() !== '') {
            taskText.textContent = newText.trim();
        }
        return;
    }
});

// Separate delegation for checkbox changes (change event)
taskList.addEventListener('change', (e) => {
    if (e.target.classList.contains('complete-checkbox')) {
        const li = e.target.parentElement;
        if (e.target.checked) {
            li.classList.add('completed');
        } else {
            li.classList.remove('completed');
        }
    }
});