$version = '3.0.1'

$packageArgs = @{
  packageName   = $env:ChocolateyPackageName
  unzipLocation = "$(Split-Path -parent $MyInvocation.MyCommand.Definition)"
  url           = "https://github.com/micronaut-projects/micronaut-starter/releases/download/v$version/mn-win-amd64-v$version.zip"
  checksum      = '8A66B58074422DC7C81642075AC38B33170C50E0C9E7668ECCBA2BD753B22F20'
  checksumType  = 'sha256'
}

Install-ChocolateyZipPackage @packageArgs
