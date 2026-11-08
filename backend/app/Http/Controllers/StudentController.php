<?php

namespace App\Http\Controllers;

use App\Models\Student;
use Illuminate\Http\JsonResponse;
use Illuminate\Http\Request;

class StudentController extends Controller
{
    public function index(Request $request): JsonResponse
    {
        $query = Student::query();
        $keyword = trim((string) $request->query('q', ''));

        if ($keyword !== '') {
            $query->where(function ($builder) use ($keyword) {
                $builder->where('student_no', 'like', "%{$keyword}%")
                    ->orWhere('name', 'like', "%{$keyword}%")
                    ->orWhere('class_name', 'like', "%{$keyword}%");
            });
        }

        $students = $query->orderByDesc('id')->paginate(
            min(max((int) $request->query('per_page', 10), 1), 100)
        );

        return response()->json([
            'data' => $students->items(),
            'current_page' => $students->currentPage(),
            'last_page' => $students->lastPage(),
            'per_page' => $students->perPage(),
            'total' => $students->total(),
        ]);
    }

    public function store(Request $request): JsonResponse
    {
        $student = Student::create($this->validatedData($request));

        return response()->json(['data' => $student], 201);
    }

    public function show(Student $student): JsonResponse
    {
        return response()->json(['data' => $student]);
    }

    public function update(Request $request, Student $student): JsonResponse
    {
        $student->update($this->validatedData($request, $student));

        return response()->json(['data' => $student->fresh()]);
    }

    public function destroy(Student $student): JsonResponse
    {
        $student->delete();

        return response()->json(['message' => '学生信息已删除']);
    }

    private function validatedData(Request $request, ?Student $student = null): array
    {
        $uniqueStudentNo = 'unique:students,student_no';
        if ($student) {
            $uniqueStudentNo .= ','.$student->id;
        }

        return $request->validate([
            'student_no' => ['required', 'string', 'max:32', $uniqueStudentNo],
            'name' => ['required', 'string', 'max:80'],
            'gender' => ['nullable', 'in:男,女,其他'],
            'class_name' => ['required', 'string', 'max:100'],
            'phone' => ['nullable', 'string', 'max:30'],
            'email' => ['nullable', 'email', 'max:150'],
        ]);
    }
}
