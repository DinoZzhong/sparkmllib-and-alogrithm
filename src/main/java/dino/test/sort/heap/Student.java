package dino.test.sort.heap;

/**
 * Created by Dino on 2016/7/4.
 */
public class Student implements Comparable<Student> {
    private String name ;
    private Double score;

    @Override
    public String toString() {
        return "Student{" +
                "name='" + name + '\'' +
                ", score=" + score +
                '}';
    }

    public Student(String name,Double score) {
        this.name = name;
        this.score =score;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }

    @Override
    public int compareTo(Student o) {
        return this.score<o.score?1:-1;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Student student = (Student) o;
        if (name != null ? !name.equals(student.name) : student.name != null) return false;
        return !(score != null ? !score.equals(student.score) : student.score != null);
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (score != null ? score.hashCode() : 0);
        return result;
    }
}
