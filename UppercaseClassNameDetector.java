import edu.umd.cs.findbugs.BugInstance;
import edu.umd.cs.findbugs.BugReporter;
import edu.umd.cs.findbugs.Detector;
import edu.umd.cs.findbugs.ba.ClassContext;

public class UppercaseClassNameDetector implements Detector {

    private final BugReporter bugReporter;

    public UppercaseClassNameDetector(BugReporter bugReporter) {
        this.bugReporter = bugReporter;
    }

    @Override
    public void visitClassContext(ClassContext classContext) {
        String className = classContext.getClassDescriptor().getClassName();
        if (Character.isUpperCase(className.charAt(0))) {
            bugReporter.reportBug(createBugInstance(classContext));
        }
    }

    @Override
    public void report() {

    }

    private BugInstance createBugInstance(ClassContext classContext) {
        return new BugInstance(this, "UPPERCASE_CLASS_NAME", HIGH_PRIORITY);
    }
}
