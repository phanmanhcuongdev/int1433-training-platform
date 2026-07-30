package vn.edu.ptit.int1433.training.runner;

import vn.edu.ptit.int1433.training.entity.Exercise;

public interface JavaCodeRunner {
    RunnerResult judge(Exercise exercise, String sourceCode);
}
