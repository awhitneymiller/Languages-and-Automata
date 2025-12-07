using Distributed
addprocs(parse(Int, ARGS[1]))

@everywhere function approximate_pi(trials::Int)
    hits = 0
    for i in 1:trials
        hits += (rand()^2 + rand()^2 < 1) ? 1 : 0
    end
    return hits
end

function main()
    total_trials = 500_000_000
    trials_per_worker = div(total_trials, nworkers())
    hits = pmap(w -> approximate_pi(trials_per_worker), workers())
    return 4 * sum(hits) / total_trials
end

println("Estimating π with $(nworkers()) workers...")
@time estimate = main()
println("π ≈ $estimate")

#=
Test 1:  2.450530 seconds
Test 2:  1.980128 seconds
Test 3:  1.999061 seconds
Test 4:  1.699530 seconds
Test 5:  1.746073 seconds
Test 6:  1.812297 seconds
Test 7:  1.818356 seconds
Test 8:  1.935760 seconds
Test 9:  1.985275 seconds
Test 10:  2.096707 seconds
Test 11:  2.302684 seconds
Test 12:  2.749943 seconds
Test 13:  2.613826 seconds
Test 14:  2.741916 seconds
Test 15:  2.941907 seconds
Test 16:  3.324478 seconds
Test 17:  3.258264 seconds
Test 18:  3.374910 seconds
Test 19:  3.682980 seconds
Test 20:  3.813601 seconds
=#
