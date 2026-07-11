import os
import subprocess

def get_conflicted_files():
    result = subprocess.run(['git', 'diff', '--name-only', '--diff-filter=U'], capture_output=True, text=True)
    if result.returncode != 0:
        print("Error getting conflicted files")
        return []
    return [f.strip() for f in result.stdout.split('\n') if f.strip()]

def merge_keep_both(filepath):
    try:
        with open(filepath, 'r', encoding='utf-8') as f:
            lines = f.readlines()
    except Exception as e:
        print(f"Error reading {filepath}: {e}")
        return False

    new_lines = []
    in_conflict = False
    in_head = False
    in_theirs = False
    head_content = []
    theirs_content = []

    for line in lines:
        if line.startswith('<<<<<<< HEAD') or line.startswith('<<<<<<<'):
            in_conflict = True
            in_head = True
            head_content = []
            theirs_content = []
        elif line.startswith('======='):
            if in_conflict:
                in_head = False
                in_theirs = True
            else:
                new_lines.append(line)
        elif line.startswith('>>>>>>>'):
            if in_conflict:
                in_conflict = False
                in_theirs = False
                # Keep both!
                new_lines.extend(head_content)
                new_lines.extend(theirs_content)
            else:
                new_lines.append(line)
        else:
            if in_head:
                head_content.append(line)
            elif in_theirs:
                theirs_content.append(line)
            else:
                new_lines.append(line)

    try:
        with open(filepath, 'w', encoding='utf-8') as f:
            f.writelines(new_lines)
        print(f"Merged keeping both for: {filepath}")
        return True
    except Exception as e:
        print(f"Error writing {filepath}: {e}")
        return False

conflicted_files = get_conflicted_files()
print(f"Found {len(conflicted_files)} conflicted files.")

for f in conflicted_files:
    if merge_keep_both(f):
        subprocess.run(['git', 'add', f])
print("Done merging conflicts.")
