package lazy.schedulerdemo;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.task.TaskSchedulingAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.PlatformTransactionManager;

@SpringBootApplication//(exclude = TaskSchedulingAutoConfiguration.class)
@EnableBatchProcessing
public class SchedulerDemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(SchedulerDemoApplication.class, args);
	}

	@Bean
	Job job(JobRepository jobRepository, Step step) {
		return new JobBuilder("job").repository(jobRepository)
				.start(step)
				.incrementer(new RunIdIncrementer())
				.build();
	}

	@Bean
	Step step(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
		return new StepBuilder("step").repository(jobRepository).transactionManager(transactionManager)
				.tasklet((contribution, chunkContext) -> RepeatStatus.FINISHED)
				.build();
	}

}
