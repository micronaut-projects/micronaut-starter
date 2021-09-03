$version = '2.5.13'

$packageArgs = @{
  packageName   = $env:ChocolateyPackageName
  unzipLocation = "$(Split-Path -parent $MyInvocation.MyCommand.Definition)"
  url           = "https://github.com/micronaut-projects/micronaut-starter/releases/download/v$version/mn-win-amd64-v$version.zip"
  checksum      = 'E7263DA641EB4E23DB330806DFA987EEDF47A235BEE22F27061BD662EE68C4BF'
  checksumType  = 'sha256'
}

Install-ChocolateyZipPackage @packageArgs
