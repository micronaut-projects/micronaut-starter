$version = '3.9.0'

$packageArgs = @{
  packageName   = $env:ChocolateyPackageName
  unzipLocation = "$(Split-Path -parent $MyInvocation.MyCommand.Definition)"
  url           = "https://github.com/micronaut-projects/micronaut-starter/releases/download/v$version/mn-win-amd64-v$version.zip"
  checksum      = 'FE9923835E671D027DEF51EA79FFA3AF9DD09F479D7A3C4555C6FBF04EE64AE0'
  checksumType  = 'sha256'
}

Install-ChocolateyZipPackage @packageArgs
